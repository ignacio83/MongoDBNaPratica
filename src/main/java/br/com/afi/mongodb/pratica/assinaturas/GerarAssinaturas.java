package br.com.afi.mongodb.pratica.assinaturas;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.MongoException.DuplicateKey;
import com.mongodb.ServerAddress;

public class GerarAssinaturas {
	private static final int[] ANOS = new int[]{2010,2011,2012,2013};
	private static final int MAX_EDICAO_ANO = 53;
	private static final int QTD_ASSINANTES = 10000000;
	private static final int QTD_TENTATIVAS = 10;
	private static final int WAIT = 5000;
	private static DBCollection projetosCollection;
	private static DBCollection assinaturasCollection;
	
	public static void main(String...args) throws Exception{
		if(args.length==0){
			System.out.println("Informe pelo menos um endereço do servidor MongoDB");
		}
		else{
			final List<ServerAddress> servers = new ArrayList<ServerAddress>();
			
			for(String arg : args){
				servers.add(new ServerAddress(arg));	
			}
			
			/*servers.add(new ServerAddress("localhost:27001"));
			servers.add(new ServerAddress("localhost:27002"));
			servers.add(new ServerAddress("localhost:27003"));*/
			
			final MongoClient mongoClient = new MongoClient(servers);
			final DB db = mongoClient.getDB("internet");
			projetosCollection = db.getCollection("projetos");
			assinaturasCollection = db.getCollection("assinaturas");
			
			final Integer[] codProjetos = getCodProjetos();
			System.out.println("Inserindo assinaturas...");
			insertAssinaturasRandom(codProjetos);
		}
		
	}
	
	private static void insertAssinaturasRandom(Integer[] codProjetos){
		int qtd = 0;
		for(int i=0; i< QTD_ASSINANTES;i++){			
			final int codPessoa = i+1;
			final int qtdAssinaturas = RandomUtils.nextInt(9);
			
			for(int j =0;j<qtdAssinaturas;j++){
				final int randomIndex = RandomUtils.nextInt(codProjetos.length);
				final int codProjeto = codProjetos[randomIndex];
				final int numEdicaoFinal = getRandomNumEdicaoFinal();
				final int numEdicaoInicial = getRandomNumEdicaoInicial(numEdicaoFinal);
				
				if(numEdicaoFinal < numEdicaoInicial){
					System.out.println("Erro " + numEdicaoInicial + " - " + numEdicaoFinal);
				}
				
				insertAssinatura(codPessoa, codProjeto, j+1, numEdicaoInicial, numEdicaoFinal);
				qtd++;
				
				if(qtd%100000 == 0){
					System.out.println(qtd + " assinaturas inseridas...");
				}
			}
		}
	}
	
	private static int getRandomNumEdicaoFinal(){
		final int anoIndex = RandomUtils.nextInt(ANOS.length);
		final int ano = ANOS[anoIndex];
		int edicao = RandomUtils.nextInt(MAX_EDICAO_ANO);
		if(edicao==0){
			edicao=1;
		}
		
		final String numEdicao = ano + StringUtils.leftPad(Integer.toString(edicao), 3, "0");
		return Integer.parseInt(numEdicao);
	}
	
	
	private static int getRandomNumEdicaoInicial(int numEdicaoFinal){
		final String numEdicaoFinalStr = String.valueOf(numEdicaoFinal);
		final int anoFinal = Integer.parseInt(numEdicaoFinalStr.substring(0,4));
		final int edicaoFinal = Integer.parseInt(numEdicaoFinalStr.substring(5,7));
		int indexAnoFinal = 0;
		
		for(int i=0; i< ANOS.length;i++){
			if(ANOS[i] == anoFinal){
				indexAnoFinal = i;
				break;
			}
		}
		
		final int anoIndex = RandomUtils.nextInt(indexAnoFinal + 1);
		final int ano = ANOS[anoIndex];
		
		int edicao;
		if(ano==anoFinal){
			edicao = RandomUtils.nextInt(edicaoFinal);
			if(edicao==0){
				edicao=1;
			}
		}
		else{
			edicao = RandomUtils.nextInt(MAX_EDICAO_ANO);
			if(edicao==0){
				edicao=1;
			}
		}
		
		final String numEdicao = ano + StringUtils.leftPad(Integer.toString(edicao), 3, "0");
		return Integer.parseInt(numEdicao);
	}
	
	private static void insertAssinatura(Integer codPessoa, Integer codProjeto, Integer numSequencia, Integer numEdicaoInicial, Integer numEdicaoFinal){		
		final BasicDBObject id = new BasicDBObject();
		id.append("codPessoa",codPessoa);
		id.append("codProjeto",codProjeto);
		id.append("numSequencia",numSequencia);
		
		final BasicDBObject assinatura = new BasicDBObject();
		assinatura.put("_id", id);
		assinatura.put("numEdicaoInicial", numEdicaoInicial);
		assinatura.put("numEdicaoFinal", numEdicaoFinal);
		
		//Tentativas em caso de erro.
		for(int retry=0;retry<QTD_TENTATIVAS;retry++){
			try{
				assinaturasCollection.insert(assinatura);
				break;
			}
			catch(DuplicateKey ex){
				System.out.println(ex.getMessage());
			}
			catch(MongoException ex){
				System.out.println(ex.getMessage());
				System.out.println("Tentativa " + retry + ". Tentando novamente em 5 segundos");
				sleep();	
			}
		}
	}
	
	private static void sleep(){
		try {
			Thread.sleep(WAIT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	private static Integer[] getCodProjetos(){
		final BasicDBObject query = new BasicDBObject("indAtivo", true);
		final BasicDBObject fields = new BasicDBObject("_id", true);
		query.append("projetosFilhos", new BasicDBObject("$exists", false));
		
		//db.projetos.find({indAtivo : true, projetosFilhos : {$exists : false  },{_id: 1}}));
		final DBCursor cursor = projetosCollection.find(query,fields);
		
		final List<Integer> list = new ArrayList<Integer>();
		while(cursor.hasNext()){
			final DBObject codProjeto = cursor.next();
			list.add((Integer) codProjeto.get("_id"));
		}
		return list.toArray(new Integer[0]);
	}
}
