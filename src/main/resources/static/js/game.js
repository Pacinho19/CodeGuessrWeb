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
         var xhr = new XMLHttpRequest();
         var url = "/code-guessr/code";
         xhr.open("POST", url, true);
         xhr.setRequestHeader("Content-Type", "application/json");
         xhr.onreadystatechange = function () {
             if (xhr.readyState === 4 && xhr.status === 200) {
                $("#codeContent").replaceWith(xhr.responseText);
             }
         };
         var data = JSON.stringify({"name":fileName, "parent": parent});
         xhr.send(data);
    }