var options = {
    series: [
        {
            name: "Series 1",
            data: [80, 50, 30, 40, 100, 20]
        },
        {
            name: "Series 2",
            data: [20, 30, 40, 80, 20, 80]
        },
        {
            name: "Series 3",
            data: [44, 76, 78, 13, 43, 10]
        }
    ],
    chart: {
        height: 350,
        type: "radar"
    },
    title: {
        text: "Radar Chart - Multi Series"
    },

    xaxis: {
        categories: ["2011", "2012", "2013", "2014", "2015", "2016"]
    },

    tooltip: {
        shared: true,
        intersect: false
    }
};
var chart = new ApexCharts(document.querySelector("#chart"), options);
chart.render();


// -------------------salesperrevenuegraph----------------//
var options = {
    chart: {
        height: 320,
        type: "line"
    },
    dataLabels: {
        enabled: true,
        enabledOnSeries: [1],
        formatter: function (val) {
            return val + "%";
        }
    },
    stroke: {
        curve: "straight",
        width: [0, 4]
    },
    title: {
        text: "Organic Sessions",
        align: "center"
    },
    series: [
        {
            name: "Sessions",
            type: "column",
            data: [
                108,
                22,
                43,
                130,
                22,
                108,
                259,
                303,
                368,
                259,
                476,
                865,
                1059,
                1167,
                2075,
                2443
            ]
        },
        {
            name: "Bounce rate",
            type: "line",
            data: [100, 100, 100, 83, 0, 80, 50, 64, 23, 67, 59, 80, 86, 85, 76, 86]
        }
    ],
    xaxis: {
        categories: [
            "2020-09-01",
            "2020-11-01",
            "2020-12-01",
            "2021-01-01",
            "2021-02-01",
            "2021-03-01",
            "2021-04-01",
            "2021-05-01",
            "2021-06-01",
            "2021-07-01",
            "2021-08-01",
            "2021-09-01",
            "2021-10-01",
            "2021-11-01",
            "2021-12-01",
            "2022-01-01"
        ],
        type: "datetime"
    },
    yaxis: [
        {
            title: {
                text: "Sessions"
            }
        },
        {
            title: {
                text: "Bounce Rate"
            },
            opposite: true,
            labels: {
                formatter: function (val) {
                    return val + "%";
                }
            }
        }
    ],
    legend: {
        position: "top"
    },
};

var avg_revenu_chart = new ApexCharts(document.querySelector("#avg-revenu-chart"), options);

avg_revenu_chart.render();