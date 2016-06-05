var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
app.get('/',function(req, res){
    res.sendFile(__dirname+'/index.html');
})
io.on('connection', function(socket){
    console.log('one user connected : '+socket.id);
    socket.on('new message',function(data){
        console.log('message : '+data)
            var sockets = io.sockets.sockets;
            sockets.forEach(function(sock){
                sock.emit('new message',{message:data});
            })
        })
    socket.on('disconnect',function(){
        console.log('one user disconnected '+socket.id);
    })
})
http.listen(3000, function(){
    console.log('server listen on port 3000')
});
