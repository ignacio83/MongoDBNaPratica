db.projetos.aggregate([
   {
      $match : {
         indAtivo : true,
         projetosFilhos : { $exists : false  }
      }
   }
   ,
   {	
      $group : {
         _id : {
            periodidade : "$codPeriodidade"
         },
         projetos : {
        	 $addToSet : "$nomProjeto"
         }
      }
    }
]);

db.projetos.aggregate([
   {
      $match : {
         indAtivo : true,
         projetosFilhos : { $exists : false  }
      }
   }
   ,
   {	
      $group : {
         _id : {
            periodidade : "$codPeriodidade"
         },
         projetos : {
        	 $addToSet : "$_id"
         }
      }
    }
]);