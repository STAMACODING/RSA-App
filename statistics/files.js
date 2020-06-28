const dataset = {"data": [{"key": "Files", "color": "#9400d3", "type": "line", "yAxis": 1, "values": [{"x": 1589673600000, "y": 1.8571428571428572}, {"x": 1590278400000, "y": 6.470588235294118}, {"x": 1590883200000, "y": 30.0}, {"x": 1591488000000, "y": 44.857142857142854}, {"x": 1592092800000, "y": 47.9375}, {"x": 1592697600000, "y": 47.9375}, {"x": 1593302400000, "y": 57.25}]}, {"key": "Lines", "color": "#d30094", "type": "line", "yAxis": 2, "values": [{"x": 1589673600000, "y": 246}, {"x": 1590278400000, "y": 505}, {"x": 1590883200000, "y": 1449}, {"x": 1591488000000, "y": 1587}, {"x": 1592092800000, "y": 1805}, {"x": 1592697600000, "y": 1805}, {"x": 1593302400000, "y": 2287}]}]}
// Setup the chart
nv.addGraph(function() {
	var chart = nv.models.multiChart()
		.margin({left: 60, right: 60});
	chart.yAxis1.options({axisLabel: "Files"});
	chart.yAxis2.options({axisLabel: "Lines"});
	chart.xAxis
		.tickFormat(function(d) { return d3.time.format('%Y-%m')(new Date(d)); })
		.options({rotateLabels: -45})

	d3.select('#chart_files svg').datum(dataset.data).call(chart);
	return chart;
});