//var c_assinaturas = db.assinaturas.find().sort({"_id.codPessoa" : 1}).skip(100000).limit(100000);
var c_assinaturas = db.assinaturas.find().sort({"_id.codPessoa" : 1}).limit(100000);

while(c_assinaturas.hasNext()){
	var a = c_assinaturas.next();	
	//Itera entre as edições
	var array_edicoes = new Array();
	for(var edicao=a.numEdicaoInicial;edicao<=a.numEdicaoFinal;edicao++){
		array_edicoes.push(edicao);
	}
	db.acessos.update({
        _id: a._id
     },
     {
         $set: { edicoes : array_edicoes }
     },
     { upsert : true	});
};