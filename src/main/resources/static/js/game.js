    $(document).ready(function () {
        $.ajax({
            "type": 'get',
            "url": '/code-guessr/nodes',
            "dataType": "json",
            "success": function (data) {
                $.each(data, function (idx, obj) {
                    $("#treeTable").append("<tr onclick=\"fileAction('"+obj.name+"','" + obj.pid +"')\" style=\"text-align:left\"data-tt-id=\"" + obj.nodeId + "\" data-tt-parent-id=\"" + obj.pid + "\"><td><a href=\"#\" onclick=\"fileAction('"+obj.name+"','" + obj.pid +"')\">" + obj.name + "</a></td><td style=\"text-align:right;\" id=\""+obj.name+"#"+obj.pid+"\"></td></tr>");
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

    function setSelected(fileName, parent){
            var selectedIconOld = document.getElementById("selectedIconId");
            if(selectedIconOld!=null){
                selectedIconOld.remove();
            }

             var selectedCol = document.getElementById(fileName + "#"+ parent);
             selectedCol.innerHTML = '<i class="bi bi-arrow-left-square-fill" aria-hidden="true" id="selectedIconId" style="font-size:20px;color:green;"></i>';
    }

    function fileAction(fileName, parent){
         setSelected(fileName, parent);


         document.getElementById("selectedFile").value = '';
         var xhr = new XMLHttpRequest();
         var url = "/code-guessr/code";
         xhr.open("POST", url, true);
         xhr.setRequestHeader("Content-Type", "application/json");
         xhr.onreadystatechange = function () {
             if (xhr.readyState === 4 && xhr.status === 200) {
                $("#codeContent").replaceWith(xhr.responseText);
                document.getElementById("selectedFile").value = parent + '/' + fileName;
             }
         };
         var data = JSON.stringify({"name":fileName, "parent": parent});
         xhr.send(data);
    }