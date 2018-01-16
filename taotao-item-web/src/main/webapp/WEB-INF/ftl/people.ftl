<html>
    <head></head>
    <body>
        名字:${people.name}<br>
        年龄:${people.age}<br>
        地址:${people.address}<br>
        <table border="1">
            <tr>
            	<th>序号</th>
                <th>名字</th>
                <th>年龄</th>
                <th>地址</th>
            </tr>
            <#list peopleList as peo>
                <#if peo_index%2 == 0 ><!--偶数行-->
            		<tr bgcolor="yellow">
            		<#else>
            		  <tr bgcolor="red">
                </#if>
                <td>${peo_index}</td>
                <td>${peo.name}</td>
                <td>${peo.age}</td>
                <td>${peo.address}</td>
                <tr>
            </#list>
        </table>
        ${date?string("yyyy/MM/dd/HH/mm/ss")}
        
        null的处理：${val!}
        <br>
        	用if处理null
        <#if val??>
		val isn't null
		<#else>
		val is null 
		</#if>
		
		<#include "hello.ftl" >
    </body>
</html>