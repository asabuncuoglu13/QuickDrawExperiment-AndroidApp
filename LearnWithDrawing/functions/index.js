const functions = require('firebase-functions');
const admin = require('firebase-admin');
var request = require('request');

var API_KEY = "AIzaSyBwAzNKqeB_BrSv8gS0yQZGsGL8cCBTh-s"; // Your Firebase Cloud Messaging Server API key

// Initialize the app with a service account, granting admin privileges
admin.initializeApp(functions.config().firebase);
ref = admin.database().ref();

exports.listenForNotificationRequests = functions.database.ref('/topics/listener/{username}');
exports.sendNotificationToUser = functions.database.ref('/topics/sender/{username}');

exports.listenForNotificationRequests = functions.database.ref('/topics/listener/{username}')
  .onWrite(event => {
    var requests = ref.child('notificationRequests');
    requests.on('child_added', function (requestSnapshot) {
      var request = requestSnapshot.val();
      sendNotificationToUser(
        request.username,
        request.message,
        function () {
          requestSnapshot.ref.remove();
        }
      );
    }, function (error) {
      console.error(error);
    });
  });

exports.sendNotificationToUser = functions.database.ref('/topics/sender/{username}')
  .onWrite(event => {
    request({
      url: 'https://fcm.googleapis.com/fcm/send',
      method: 'POST',
      headers: {
        'Content-Type': ' application/json',
        'Authorization': 'key=' + API_KEY
      },
      body: JSON.stringify({
        notification: {
          title: message
        },
        to: '/topics/user_' + username
      })
    }, function (error, response, body) {
      if (error) { console.error(error); }
      else if (response.statusCode >= 400) {
        console.error('HTTP Error: ' + response.statusCode + ' - ' + response.statusMessage);
      }
      else {
        onSuccess();
      }
    });
  });