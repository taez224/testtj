<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
  <!--   <meta charset="euc-kr">--> 
    <title>chart_json</title>
    <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
</head>

<body>
<div style="width:650px;margis:autp">
    <div><input type="button" value="click" id="jsonLoad"></div>
    <div><input type="button" value="click2" id="jsonLoad2"></div>
    <div><input type="button" value="click3" id="jsonLoad3"></div>
     <div>
        <form action="">
            {% csrf_token %}
        <input type="text" id="useYymm">
        <input type="radio" name="sxClNm" value="%EC%97%AC">여
        <input type="radio" name="sxClNm" value="%EB%82%A8">남
            <input type="button" id="wrtBtn" value="등록">
        </form>
    </div>
    <div id="target"></div>
    <!-- chart 출력위한 UI -->
    <div id="graph2"></div>
    <div id="chart1"></div>
     <div class="chart" id="graph">
    </div>
</div>
<!-- C3JS 라이브러리 불러오기 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://d3js.org/d3.v3.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/c3/0.4.11/c3.min.js"></script>

<script>
   $(function(){
	   
       $('#jsonLoad').click(function(){
          $.ajax({
          url:'http://192.168.0.6:8099/shop/loadjsonp?callback',
//           url:'http://52.79.242.175:9000/shop/loadjson',
          type:'GET',
          dataType:'jsonp',
          jsonp:'callback',
          success:function(data){
              console.log(data.columns);
              console.log('***************');
              console.log(data.data);
              makeBarChart(data.data,data.columns);
          },
          error:function(data){
        	  console.log("error>>"+$('#target').text())
          }
          });
       }); // click func end!
       function makeBarChart(jsonData,dcol){
           var datas = [];
           // [n개의 컬럼]
           var dcolumns = dcol.slice(1, dcol.length);
           // console.log(dcolumns);
           for(var key in jsonData){
               datas.push(jsonData[key]);
           }
           console.log('=============');
           console.log(datas);
           var chart = c3.generate({
                bindto: "#chart1",
                data : {
                    columns : datas,
                    type: "bar"
                },
                bar: {
                    width: {
                        ratio: 0.5
                    }
                },
                axis: {
                    x: {
                        type: 'category',
                        categories: dcolumns
                    },
                    y: {
                        label: {
                          text: '점수',
                          position: 'outer-middle'
                        },

                    }
                },

            });
       } // func end
       $('#jsonLoad2').click(function(){
          $.ajax({
          url:'http://192.168.0.6:8099/shop/loadjsonp2?callback',
//           url:'http://52.79.242.175:9000/shop/loadjson2',
          type:'GET',
          dataType:'jsonp',
          jsonp:'callback',
          success:function(data){
              console.log(data);
              Plotly.plot('graph', data, {});
          },
          error:function(data){
        	  console.log("error>>"+$('#target').text())
          }
          });
       }); // click func end!
       $('#jsonLoad3').click(function(){
           $.ajax({
           url:'http://192.168.0.6:8099/shop/loadjsonp3?callback',
           type:'GET',
           dataType:'jsonp',
           jsonp:'callback',
           success:function(data){
               console.log(data);
               Plotly.plot('graph', data, {});
           },
           error:function(data){
         	  console.log("error>>"+$('#target').text())
           }
           });
        }); // click func end!
    });
</script>
</body>
</html>
