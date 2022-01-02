var stompClient = null;

function loadAll() {
    if (stompClient.connected) {
        stompClient.send("/app/all", {}, null);
    } else {
        console.log('NOT CONNECTED');
    }
}
function loadTable(data) {
    const table = document.getElementById("clientsTableBody");
    var items = JSON.parse(data.body);
    $("#clientsTableBody tr").remove()
    items.forEach( item => {
        let row = table.insertRow();
        let date = row.insertCell(0);
        date.innerHTML = item.id;
        let login = row.insertCell(1);
        login.innerHTML = item.login;
        let password = row.insertCell(2);
        password.innerHTML = item.password;
        let phone = row.insertCell(3);
        phone.innerHTML = item.phone;
        let address = row.insertCell(4);
        address.innerHTML = item.address;
    });
}

function connect() {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));

    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/clients/page', (data) => loadTable(data));
    });
}

function init() {
    connect();
    setTimeout(() => loadAll(), 100);
}

function create() {
    stompClient.send("/app/create", {}, JSON.stringify({
        'login' : $("#loginTextBox").val(),
        'password' : $("#passwordTextBox").val(),
        'phone' : $("#phoneTextBox").val(),
        'address' : $("#citySelectBox").val()
    }));
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#create" ).click(function() { create(); });
});

