// Setup the chart
const contribution = {}
nv.addGraph(function() {
	var chart = nv.models.pieChart()
		.x(function(d) { return d.key })
		.y(function(d) { return d.y })
		.options({
                    "padAngle": 0.01,
                    "cornerRadius": 5
                });
	chart.pie.donutLabelsOutside(true).donut(true);

	d3.select('#chart_contribution svg').datum(contribution.data).call(chart);
	return chart;
});

const lines_stats = {"data": [{"key": "harrydehix", "values": [{"x": 1589673600000, "y": 0}, {"x": 1590278400000, "y": 319}, {"x": 1590883200000, "y": 1797}, {"x": 1591488000000, "y": 1898}, {"x": 1592092800000, "y": 2075}, {"x": 1592697600000, "y": 2075}, {"x": 1593302400000, "y": 2337}]}, {"key": "BeneCodecademy", "values": [{"x": 1589673600000, "y": 0}, {"x": 1590278400000, "y": 0}, {"x": 1590883200000, "y": 123}, {"x": 1591488000000, "y": 180}, {"x": 1592092800000, "y": 365}, {"x": 1592697600000, "y": 365}, {"x": 1593302400000, "y": 365}]}, {"key": "MrTK", "values": [{"x": 1589673600000, "y": 239}, {"x": 1590278400000, "y": 284}, {"x": 1590883200000, "y": 553}, {"x": 1591488000000, "y": 574}, {"x": 1592092800000, "y": 574}, {"x": 1592697600000, "y": 574}, {"x": 1593302400000, "y": 574}]}, {"key": "Henri Hollmann", "values": [{"x": 1589673600000, "y": 0}, {"x": 1590278400000, "y": 6}, {"x": 1590883200000, "y": 22}, {"x": 1591488000000, "y": 24}, {"x": 1592092800000, "y": 24}, {"x": 1592697600000, "y": 24}, {"x": 1593302400000, "y": 24}]}, {"key": "rHedBull", "values": [{"x": 1589673600000, "y": 21}, {"x": 1590278400000, "y": 21}, {"x": 1590883200000, "y": 118}, {"x": 1591488000000, "y": 118}, {"x": 1592092800000, "y": 118}, {"x": 1592697600000, "y": 118}, {"x": 1593302400000, "y": 118}]}, {"key": "saltyMrTK", "values": [{"x": 1589673600000, "y": 0}, {"x": 1590278400000, "y": 44}, {"x": 1590883200000, "y": 44}, {"x": 1591488000000, "y": 44}, {"x": 1592092800000, "y": 44}, {"x": 1592697600000, "y": 44}, {"x": 1593302400000, "y": 44}]}, {"key": "Hendrik Wirthwein", "values": [{"x": 1589673600000, "y": 0}, {"x": 1590278400000, "y": 0}, {"x": 1590883200000, "y": 0}, {"x": 1591488000000, "y": 0}, {"x": 1592092800000, "y": 0}, {"x": 1592697600000, "y": 0}, {"x": 1593302400000, "y": 475}]}, {"key": "Benedikt Schn\u00f6rr", "values": [{"x": 1589673600000, "y": 0}, {"x": 1590278400000, "y": 8}, {"x": 1590883200000, "y": 8}, {"x": 1591488000000, "y": 8}, {"x": 1592092800000, "y": 8}, {"x": 1592697600000, "y": 8}, {"x": 1593302400000, "y": 8}]}, {"key": "MeineHTMLCodes", "values": [{"x": 1589673600000, "y": 0}, {"x": 1590278400000, "y": 62}, {"x": 1590883200000, "y": 62}, {"x": 1591488000000, "y": 62}, {"x": 1592092800000, "y": 62}, {"x": 1592697600000, "y": 62}, {"x": 1593302400000, "y": 62}]}, {"key": "Others", "values": [{"x": 1589673600000, "y": 0}, {"x": 1590278400000, "y": 0}, {"x": 1590883200000, "y": 0}, {"x": 1591488000000, "y": 0}, {"x": 1592092800000, "y": 0}, {"x": 1592697600000, "y": 0}, {"x": 1593302400000, "y": 0}]}]}
// Setup the lines by author chart
nv.addGraph(function() {
	var chart = nv.models.lineChart()
		.useInteractiveGuideline(true);
	chart.yAxis.options({ "axisLabel": "Lines" });
	chart.xAxis
		.tickFormat(function(d) { return d3.time.format('%Y-%m')(new Date(d)); })
		.options({ "rotateLabels": -45 })

	d3.select('#chart_loc svg').datum(lines_stats.data).call(chart);
	return chart;
});

const commit_stats = {"data": [{"key": "harrydehix", "values": [[1589673600000, 0, 0], [1590278400000, 4, 4], [1590883200000, 22, 18], [1591488000000, 25, 3], [1592092800000, 33, 8], [1592697600000, 33, 0], [1593302400000, 34, 1]]}, {"key": "BeneCodecademy", "values": [[1589673600000, 0, 0], [1590278400000, 0, 0], [1590883200000, 4, 4], [1591488000000, 5, 1], [1592092800000, 17, 12], [1592697600000, 17, 0], [1593302400000, 17, 0]]}, {"key": "MrTK", "values": [[1589673600000, 2, 0], [1590278400000, 3, 1], [1590883200000, 9, 6], [1591488000000, 10, 1], [1592092800000, 10, 0], [1592697600000, 10, 0], [1593302400000, 10, 0]]}, {"key": "Henri Hollmann", "values": [[1589673600000, 0, 0], [1590278400000, 2, 2], [1590883200000, 5, 3], [1591488000000, 7, 2], [1592092800000, 7, 0], [1592697600000, 7, 0], [1593302400000, 7, 0]]}, {"key": "rHedBull", "values": [[1589673600000, 5, 0], [1590278400000, 5, 0], [1590883200000, 7, 2], [1591488000000, 7, 0], [1592092800000, 7, 0], [1592697600000, 7, 0], [1593302400000, 7, 0]]}, {"key": "saltyMrTK", "values": [[1589673600000, 0, 0], [1590278400000, 6, 6], [1590883200000, 6, 0], [1591488000000, 6, 0], [1592092800000, 6, 0], [1592697600000, 6, 0], [1593302400000, 6, 0]]}, {"key": "Hendrik Wirthwein", "values": [[1589673600000, 0, 0], [1590278400000, 0, 0], [1590883200000, 0, 0], [1591488000000, 0, 0], [1592092800000, 0, 0], [1592697600000, 0, 0], [1593302400000, 4, 4]]}, {"key": "Benedikt Schn\u00f6rr", "values": [[1589673600000, 0, 0], [1590278400000, 3, 3], [1590883200000, 3, 0], [1591488000000, 3, 0], [1592092800000, 3, 0], [1592697600000, 3, 0], [1593302400000, 3, 0]]}, {"key": "MeineHTMLCodes", "values": [[1589673600000, 0, 0], [1590278400000, 3, 3], [1590883200000, 3, 0], [1591488000000, 3, 0], [1592092800000, 3, 0], [1592697600000, 3, 0], [1593302400000, 3, 0]]}, {"key": "Others", "values": [[1589673600000, 0, 0], [1590278400000, 0, 0], [1590883200000, 0, 0], [1591488000000, 0, 0], [1592092800000, 0, 0], [1592697600000, 0, 0], [1593302400000, 0, 0]]}]}
// Setup the commits-by-author chart
nv.addGraph(function() {
	var chart = nv.models.lineChart()
		.x(function(d) { return d[0] })
		.y(function(d) { return d[1] })
		.useInteractiveGuideline(true);
	chart.yAxis.options({ "axisLabel": "Commits" });
	chart.xAxis
		.tickFormat(function(d) { return d3.time.format('%Y-%m')(new Date(d)); })
		.options({ "rotateLabels": -45 })

	d3.select('#chart_commits svg').datum(commit_stats.data).call(chart);
	return chart;
});

// Setup the streamgraph
nv.addGraph(function() {
	var chart = nv.models.stackedAreaChart()
		.x(function(d) { return d[0] })
		.y(function(d) { return d[2] })
		.options({
		        "useInteractiveGuideline": true,
		        "style": "stream-center",
                "showControls": false,
                "showLegend": false,
                });
	chart.yAxis.options({ "axisLabel": "Commits" });
	chart.xAxis
		.tickFormat(function(d) { return d3.time.format('%Y-%m')(new Date(d)); })
		.options({ "rotateLabels": -45 })

	d3.select('#chart_steam svg').datum(commit_stats.data).call(chart);
	return chart;
});

const domains = {"data": [{"key": "gmail.com", "y": 83}, {"key": "users.noreply.github.com", "y": 5}, {"key": "Wolfgangs-iMac.fritz.box", "y": 3}]}
// Setup the chart
nv.addGraph(function() {
	var chart = nv.models.pieChart()
		.x(function(d) { return d.key })
		.y(function(d) { return d.y })
		.options({
                "padAngle": 0.01,
                "cornerRadius": 5
            });
	chart.pie.donutLabelsOutside(true).donut(true);

	d3.select('#chart_domains svg').datum(domains.data).call(chart);
	return chart;
});