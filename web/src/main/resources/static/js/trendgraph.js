
$.urlParam = function(name){
    var results = new RegExp('[\?&]' + name + '=([^&]*)').exec(window.location.href);
    if (results==null){
        return null;
    }
    else{
        return results[1] || 0;
    }
};

function loadChartData(url, slug) {
    return $.ajax({
        url: url + slug + "/",
        dataType: "json"
    });
}

function loadChart(team) {
    loadChartData("/trend/chartjs/weekly/", team).done(drawHappinessChart);
}

function loadTable(team){
    console.log('showTable');
    loadChartData("/trend/weekly/", team).done(drawTable);
}

function drawTable(data) {

    var dataTable = new google.visualization.DataTable(data);

    var table = new google.visualization.Table(document.getElementById('table'));

    table.draw(dataTable);
}

var team;

$(document).ready(function(){

    var showTable = !($.urlParam == null || !$.urlParam('table') || $.urlParam('table') === 'false');

    console.log(showTable);

    if($.urlParam == null || !$.urlParam('team')){
        team = 'all';
    } else {
        team = $.urlParam('team');
    }

    if($.urlParam('success') === "true") {
        toastr.options = {
            "positionClass": "toast-top-full-width",
            "extendedTimeOut": "10000"
        };
        toastr.success('Thank you for your submission');
    }
    
    if(showTable) {
        google.charts.load('current', {'packages': ['table']});
        google.charts.setOnLoadCallback(loadTable(team));
    }
    loadChart(team);
});

function getChartConfig(data, title, yAxisLabel1, yAxisLabel2) {
    return {
        type: 'line',
        data: data,
        options: {
            title: {
                display: true,
                text: title
            },
            scales: {
                yAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: yAxisLabel1
                    },
                    type: "linear",
                    ticks: {
                        beginAtZero: true,
                        min: 0,
                        max: 5
                    },
                    position: "left",
                    id: "y-axis-1"
                },{
                    scaleLabel: {
                        display: true,
                        labelString: yAxisLabel2
                    },
                    type: "linear",
                    ticks: {
                        beginAtZero: true,
                        stepSize: 1
                    },
                    position: "right",
                    id: "y-axis-2",
                    gridLines: {
                        drawOnChartArea: false
                    }
                }],
                xAxes: [{
                    type: 'time',
                    time: {
                        unit: 'week',
                        tooltipFormat: 'll',
                        min: moment().year(moment().year() - 1)
                    }
                }]
            }
        }
    }
}

function drawHappinessChart(data) {

    var ctx = $('#chart1');

    new Chart(ctx, getChartConfig(data, "Team Happiness", "Average Rating", "Response Count"));
}