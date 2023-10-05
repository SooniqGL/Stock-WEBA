<%-- 
    Document   : testDate
    Created on : Mar 11, 2011, 9:57:48 AM
    Author     : QZ69042
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <script type="text/javascript" language="JavaScript">
            function findAge() {
                    var form = document.dateForm;

                    // stmtDate format: mmyyyy
                    var stmtDate = form.stmtDate.value;
                    var currDate = new Date();

                    var currMonth = currDate.getMonth() + 1;
                    var currYear  = currDate.getFullYear();

                    var stmtMonth = parseInt(stmtDate.substring(0, 2), 10);
                    var stmtYear = parseInt(stmtDate.substring(2), 10);

                    // debug it
                    alert(stmtMonth + ":" + stmtYear + ":" + currMonth + ":" + currYear);

                    var theAge = (currYear - stmtYear) * 12 + currMonth - stmtMonth;

                    form.age.value = theAge;
            }

            function findAge2() {
                    var form = document.dateForm;

                    // stmtDate format: mmyyyy
                    var stmtDate = form.stmtDate.value;
                    var stmtDate2 = stmtDate.substring(0, 2) + "/01/" + stmtDate.substring(2);
                    var theAge = monthsBetween(stmtDate2);

                    form.age2.value = theAge;
            }

            function monthsBetween(termStartDate) {

                var usrDate = new Date(termStartDate);
                var curDate = new Date();
                var usrYear, usrMonth = usrDate.getMonth()+1;
                var curYear, curMonth = curDate.getMonth()+1;
                if((usrYear=usrDate.getFullYear()) < (curYear=curDate.getFullYear())){
                    curMonth += (curYear - usrYear) * 12;
                }
                var diffMonths = curMonth - usrMonth;
                if(usrDate.getDate() > curDate.getDate()) diffMonths--;

                return diffMonths;
            }
        </script>
    </head>
    <body>
        <h1>Hello World!</h1>

        <form name="dateForm">
            <table>
                <tr><td>Statement Date: </td><td><input type="text" name="stmtDate"></td></tr>
                <tr><td>Age: </td><td><input type="text" name="age"></td></tr>
                <tr><td>Age2: </td><td><input type="text" name="age2"></td></tr>
                <tr><td colspan="2"><input type="button" value="Find" onclick="findAge();findAge2()"></td></tr>
            </table>
        </form>
    </body>
</html>
