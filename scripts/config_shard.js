sh.addShard("s1/localhost:27001,localhost:27002,localhost:27003");

sh.addShard("s2/localhost:27004,localhost:27005,localhost:27006");

sh.enableSharding('internet');

db.assinaturas.ensureIndex({"_id.codPessoa" : 1 });
sh.shardCollection('internet.assinaturas', {"_id.codPessoa":1});
sh.shardCollection('internet.assinaturas', {"_id.codPessoa":"hashed"});


db.acessos.ensureIndex({"_id.codPessoa" : 1 });
sh.shardCollection('internet.acessos', {"_id.codPessoa":1});
