<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.soleexpressions.ecommercestore.POJOs.Shoe" %>

<%
    List<Shoe> allShoes = (List<Shoe>) request.getAttribute("allShoes");
    if (allShoes == null) {
        allShoes = new ArrayList<>();
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customize Your Shoe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@200;300;400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/preferences.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css"/>
    <style>

    </style>
</head>
<body>
    <div class="desktop">
        <jsp:include page="/views/common/navbar.jsp"/>

        <div class="page-content-wrapper">
            <form id="preferencesForm" action="${pageContext.request.contextPath}/preferences" method="post" >
                <div class="form-container">
                    <div class="question-box q1">
                        <h1>Step #1</h1>
                        <label>Choose a shoe model:</label>
                        <select name="shoemodel" id="modelButton" class="custom-select">
                            <option selected value="">Select Model</option>
                            <% for (Shoe shoe : allShoes) { %>
                                <option value="<%= shoe.getId() %>" data-base-cost="<%= shoe.getBaseCost() %>"><%= shoe.getModel() %></option>
                            <% } %>
                        </select>
                        <input type="hidden" name="selectedShoeId" id="selectedShoeId" value=""/>
                    </div>
                    <div class="question-box q2">
                        <h1>Step #2</h1>
                        <label>Find your shoe size:</label>
                        <div class="size-container" id="sizeButton">

                        </div>
                        <input type="hidden" name="selectedSize" id="selectedSize" value=""/>
                    </div>
                    <div class="question-box q3" id="mainColorButton">
                        <h1>Step #3</h1>
                        <label>Make your own main color:</label>
                        <input type="color" id="mcolor" name="selectedBaseColor" value="#000000">
                    </div>
                    <div class="question-box q4" id="soleColorButton">
                        <h1>Step #4</h1>
                        <label>Make your own color for the sole:</label>
                        <input type="color" id="scolor" name="selectedSoleColor" value="#000000">
                    </div>
                    <div class="question-box q5">
                        <h1>Step #5</h1>
                        <label>Pick the lace color:</label>
                        <div class="lacecolor-container" id="laceColorButton">
                            <%
                                String[] laceColors = {"white", "black", "gray", "yellow", "blue", "red", "pink", "purple", "orange"};
                                for (String color : laceColors) {
                            %>
                                <div class="radio-option">
                                    <input type="radio" name="selectedLaceColor" id="js2-<%= color %>" value="<%= color %>">
                                    <span class="checkmark"></span>
                                    <div class="button-label"><%= color %></div>
                                </div>
                            <% } %>
                        </div>
                    </div>
                    <input type="hidden" name="cost" id="cost" value="0"/>
                </div>
            </form>

            <div class="summary-controls-wrapper">
                <div class="next-page">
                    <label>Are you done?</label>
                    <button type="submit" class="next">NEXT</button>
                </div>
                <div class="cost-info">
                    <label>Your custom shoe costs:</label>
                    <div class="total" id="costDisplay">0 $</div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/preferences.js"></script>
</body>
</html>