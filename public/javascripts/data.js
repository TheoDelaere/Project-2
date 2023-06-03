$(function () {
  const ctx = document.getElementById('myChart');
  var myChart;

  // Déplacez la création du graphique en dehors de la fonction update()
  myChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['Red', 'Green', 'Blue', 'Yellow', 'Orange'],
      datasets: [{
        label: 'Number of color',
        data: [],
        backgroundColor: [
          'rgba(255, 99, 132, 0.7)',
          'rgba(127, 255, 0, 0.7)',
          'rgba(54, 162, 235, 0.7)',
          'rgba(255, 205, 86, 0.7)',
          'rgba(255, 159, 64, 0.7)',
        ],
        borderColor: [
          'rgba(255, 99, 132)',
          'rgba(0, 100, 0)',
          'rgba(54, 162, 235)',
          'rgba(255, 205, 86)',
          'rgba(255, 159, 64)',
        ],
        borderWidth: 2
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });

  $("#btnStart").click(function (e) {
    e.preventDefault();
    $.ajax({
      type: "get",
      url: "http://192.168.0.13:1880/data",
      data: { status: true },
      dataType: "jsonp",
      success: function (response) {
        console.log(response);
      }
    });
  });

  $("#btnStop").click(function (e) {
    e.preventDefault();
    $.ajax({
      type: "get",
      url: "http://192.168.0.13:1880/data",
      data: { status: false },
      dataType: "jsonp",
      success: function (response) {
        console.log(response);
      }
    });
  });

  setInterval(update, 500);

  function update() {
    $.ajax({
      url: "/api/data",
      success: function (data) {
        
        // Mettez à jour les données du graphique
        myChart.data.datasets[0].data = [
          data.historyData[data.historyData.length - 1].r_tot,
          data.historyData[data.historyData.length - 1].g_tot,
          data.historyData[data.historyData.length - 1].b_tot,
          data.historyData[data.historyData.length - 1].y_tot,
          data.historyData[data.historyData.length - 1].o_tot
        ];

        // Mettez à jour le graphique
        myChart.update();
      }
    });
  }
});
