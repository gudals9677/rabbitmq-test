<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Real-Time Chat</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.1/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <style>
        .message {
            display: flex;
            align-items: flex-start;
            margin-bottom: 10px;
        }
        .sender {
            font-weight: bold;
            margin-right: 10px;
            color: #007BFF;
        }
        .content {
            word-break: break-word;
        }
    </style>
</head>
<body>
    <h1>Chat Room</h1>
    <div id="chat"></div>
    <input type="text" id="message" placeholder="Enter message" />
    <button id="send-button">Send</button>

    <script>
        $(document).ready(function () {
            var socket = new SockJS('/ws');
            console.log("socket ::", socket);
            var stompClient = Stomp.over(socket);

            // WebSocket 연결
            stompClient.connect({}, function () {
                console.log('Connected to WebSocket');

                // 서버로부터 메시지 수신
                stompClient.subscribe('/topic/messages', function (message) {
                    console.log("받은 message ::", message);

                    var messageBody = JSON.parse(message.body);
                    console.log("받은 messageBody ::", messageBody);

                    // 메시지 화면에 추가
                    addMessageToChat(messageBody.sender, messageBody.content);
                });
            });

            // 메시지 보내기 버튼 클릭 이벤트
            $('#send-button').click(function () {
                sendMessage();
            });

            // 메시지 전송 함수
            function sendMessage() {
                var message = $('#message').val();
                if (message.trim() === '') {
                    alert('메시지를 입력하세요.');
                    return;
                }

                stompClient.send("/app/send", {}, JSON.stringify({ sender: "User", content: message }));
                $('#message').val(''); // 입력 필드 초기화
            }

            // 채팅에 메시지 추가 함수
            function addMessageToChat(sender, content) {
                var messageElement = $('<div>').addClass('message');
                var senderElement = $('<span>').addClass('sender').text(sender);
                var contentElement = $('<span>').addClass('content').text(content);

                messageElement.append(senderElement).append(contentElement);
                $('#chat').append(messageElement);
            }
        });
    </script>
</body>
</html>
