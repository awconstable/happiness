
$.urlParam = function(name){
    var results = new RegExp('[\?&]' + name + '=([^&]*)').exec(window.location.href);
    if (results==null){
        return null;
    }
    else{
        return results[1] || 0;
    }
};

$("#happiness-refresh-button").click(function () {
    loadChart(team);
});

function loadChartData(url, slug) {
    return $.ajax({
        url: url + slug + "/",
        dataType: "json"
    });
}

function loadChart(team) {
    loadChartData("/trend/chartjs/weekly/", team).done(drawVisualisation);
}

function drawVisualisation(data){
    //TODO round averages
    drawHappinessChart(data);
    drawChartJsTable(data);
}

function drawChartJsTable(data){
    var tableBodyElem = $('#table-body');
    var html = '<thead><tr><th style="width:120px;">Week</th>';

    var columnCount = 0;
    jQuery.each(data.datasets, function (idx, item) {
        html += '<th style="background-color:' + item.fillColor + ';">' + item.label + '</th>';
        columnCount += 1;
    });

    html += '</tr></thead>';

    jQuery.each(data.labels, function (idx, item) {
        html += '<tr><td>' + moment(item).format('[Week] W, MMM YYYY') + '</td>';
        for (i = 0; i < columnCount; i++) {
            html += '<td style="background-color:' + data.datasets[i].fillColor + ';">' + (data.datasets[i].data[idx] === '0' ? '-' : data.datasets[i].data[idx].toFixed(2)) + '</td>';
        }
        html += '</tr>';
    });

    html += '</tr><tbody>';

    tableBodyElem.append(html);
}

var team;

$(document).ready(function(){

    feather.replace();

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
                        precision: 0
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
                        tooltipFormat: '[Week] W, MMM YYYY',
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