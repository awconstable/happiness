
$.urlParam = function(name){
    var results = new RegExp('[\?&]' + name + '=([^&]*)').exec(window.location.href);
    if (results==null){
        return null;
    }
    else{
        return results[1] || 0;
    }
};

var showTable;

if($.urlParam == null || !$.urlParam('table') || $.urlParam('table') === 'false'){
    showTable = false;
} else {
    showTable = true;
}

console.log(showTable);

google.charts.load('current', {'packages':['line', 'table']});
google.charts.setOnLoadCallback(loadChart);

var team;

if($.urlParam == null || !$.urlParam('team')){
    team = 'all';
} else {
    team = $.urlParam('team');
}

function loadChartData(url, slug) {
    return $.ajax({
        url: url + slug + "/",
        dataType: "json"
    });
}

function loadChart() {
    loadChartData("/trend/weekly/", team).done(drawChart);
    if(showTable){
        console.log('showTable');
        loadChartData("/trend/weekly/", team).done(drawTable);
    }
    loadChartData("/trend/chartjs/weekly/", team).done(drawHappinessChart);
}

function drawChart(data) {

    var dataTable = new google.visualization.DataTable(data);

    var options = {
        chart: {
            title: 'Team Happiness',
            subtitle: 'average happiness over time'
        },
        width: 900,
        height: 500,
        series: {
            // Gives each series an axis name that matches the Y-axis below.
            0: {targetAxisIndex: 0},
            1: {targetAxisIndex: 1}
        },
        vAxes: {
            // Adds titles to each axis.
            0: {title: 'Happiness (avg)', viewWindow:{max: 5, min: 0}},
            1: {title: 'Responses', viewWindow:{min: 0}, gridlines:{color: '#f2f2f2'}}
        },
        colors:['orange','grey']
    };

    var chart = new google.charts.Line(document.getElementById('linechart_material'));

    chart.draw(dataTable, google.charts.Line.convertOptions(options));
}

function drawTable(data) {

    var dataTable = new google.visualization.DataTable(data);

    var table = new google.visualization.Table(document.getElementById('table'));

    table.draw(dataTable);
}

$(document).ready(function(){
    if($.urlParam('success') === "true") {
        toastr.options = {
            "positionClass": "toast-top-full-width",
            "extendedTimeOut": "10000"
        };
        toastr.success('Thank you for your submission');
    }
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