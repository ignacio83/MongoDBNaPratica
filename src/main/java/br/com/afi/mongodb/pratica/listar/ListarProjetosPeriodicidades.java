package br.com.afi.mongodb.pratica.listar;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class ListarProjetosPeriodicidades {

	public static void main(String...args) throws Exception{
		if(args.length==0){
			System.out.println("Informe pelo menos um endereço do servidor MongoDB");
		}
		else{
			final List<ServerAddress> servers = new ArrayList<ServerAddress>();
			
			for(String arg : args){
				servers.add(new ServerAddress(arg));	
			}
			
			final MongoClient mongoClient = new MongoClient(servers);
			
			final DB db = mongoClient.getDB("internet");
			
			final DBCollection projetosCollection = db.getCollection("projetos");
			final DBCollection periodicidadesCollection = db.getCollection("periodicidades");
			
			final BasicDBObject query = new BasicDBObject("indAtivo", true);
			query.append("projetosFilhos", new BasicDBObject("$exists", false));
			
			final BasicDBObject orderBy = new BasicDBObject("nomProjeto", 1);
			
			//db.projetos.find({indAtivo : true, projetosFilhos : {$exists : false  }}).sort({nomProjeto : 1});
			final DBCursor cursor = projetosCollection.find(query).sort(orderBy);
			
			while(cursor.hasNext()){
				final DBObject projeto = cursor.next();
				final String nomProjeto = (String) projeto.get("nomProjeto");
				final String codPeriodicidade = (String) projeto.get("codPeriodidade");
				final DBObject periodicidade = periodicidadesCollection.findOne(new BasicDBObject("_id", codPeriodicidade));
				final String descricaoPeriodicidade = (String) periodicidade.get("descricao");
				
				System.out.println(nomProjeto + " - " + descricaoPeriodicidade);
			}
		}
		
	}
}
