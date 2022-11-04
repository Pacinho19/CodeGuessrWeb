    $(document).ready(function () {
        $.ajax({
            "type": 'get',
            "url": '/code-guessr/nodes',
            "dataType": "json",
            "success": function (data) {
                $.each(data, function (idx, obj) {
                    $("#treeTable").append("<tr style=\"text-align:left\"data-tt-id=\"" + obj.nodeId + "\" data-tt-parent-id=\"" + obj.pid + "\"><td><a href=\"#\" onclick=\"fileAction('"+obj.name+"','" + obj.pid +"')\">" + obj.name + "</a></td></tr>");
                });
                $("#treeTable").treetable({
                    expandable: true,
                    expanded: false,
                    clickableNodeNames: true,
                    indent: 30
                });
            }
        });
    });

    function fileAction(fileName, parent){
        console.log(fileName);
        console.log(parent);
    }