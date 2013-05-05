<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <LINK href="/minecraft-stats/resources/css/main.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript"
        	src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <script type="text/javascript">
            google.load("visualization", "1", {packages:["corechart"]});
        </script>
        <script type="text/javascript">
            $(function() {

                google.setOnLoadCallback(drawChart);



                $.ajax("/minecraft-stats/stats/running", {
                    type: 'POST',

                    complete : function(jqXHR, status) {
                        var started = JSON.parse(jqXHR.responseText);

                        if(started) {

                            $("#status").css("color", "rgb(98, 139, 93)").text("Running");

                        } else {
                            $("#status").css("color", "rgb(139, 93, 93)").text("Down");
                        }
                    }

                });
            });
            function drawChart() {
                $.ajax("/minecraft-stats/stats/frequency", {

                    type : 'POST',

                    complete : function(jqXHR, status) {

                        var response = JSON.parse(jqXHR.responseText);
                        var list = [];
                        var data = new google.visualization.DataTable();

                        // Declare columns
                        data.addColumn('datetime', 'Time');
                        data.addColumn('number', 'Count');

                        for(var i in response) {
                            list[list.length] =
                                [new Date(response[i].logTime), response[i].count];
                        }

                        data.addRows(list);

                        var options = {
                          title: 'Server Visits',
                          titleTextStyle: {color: 'white'},
                          animation: {
                            duration: 2000,
                            easing: 'in'
                          },
                          series: [{color: '#516f3a'}],
                          backgroundColor: '#252525',
                          vAxis: {
                            gridlines: {color: '#745d50'},
                            textStyle: {color: '#745d50'}
                          },
                          hAxis: {
                            gridlines: {color: '#745d50'},
                            textStyle: {color: '#745d50'}
                          },
                          legend: {
                            textStyle: {color: '#745d50'}
                          }
                        };

                        var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
                        chart.draw(data, options);


                    }
                });
            }


        </script>
    </head>
    <body>
        <section class="center-80">
            <header class="banner">
            </header>
            <nav>
                <ul/>
            </nav>
            <article class="status-section">
                <header>
                    Server Status: <span id="status"><span>
                </header>
                <div class="status">
                This page lists server details as well as logged in users

                </div>
                <hr />
                <div class="center-80">
                    <c:choose>
                        <c:when test="${!list.isEmpty()}">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Players Logged In</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="i" begin="0" end="${list.size()-1}">
                                        <tr>
                                            <td><c:out value="${list.get(i).getScreenName()}"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                        <h2>No players logged in at the moment</h2>
                        </c:otherwise>
                      </c:choose>
                </div>
                <div id="chart_div" style="margin: 100px auto; width: 100%; height: 250px;"></div>
            </article>
        </section>
    </body>
</html>
