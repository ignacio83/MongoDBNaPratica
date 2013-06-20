mkdir /home/andre/data/
mkdir /home/andre/data/r1s3

echo Executando Mongod - Nó 1 - S3
mongod --port 27007 --shardsvr --replSet s3 --dbpath /home/andre/data/r1s3 --logpath /home/andre/data/r1s3.log --smallfiles --oplogSize 100 --fork

echo Nós em execução:
ps aux | grep mongod

