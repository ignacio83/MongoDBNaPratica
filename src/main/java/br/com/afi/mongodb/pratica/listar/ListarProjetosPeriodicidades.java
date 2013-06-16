package br.com.afi.mongodb.pratica.listar;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ListarProjetosPeriodicidades {

	public static void main(String...args) throws Exception{
		final MongoClient mongoClient = new MongoClient("localhost:27001");
		
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
