<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.soleexpressions.ecommercestore.POJOs.Basket" %>
<%@ page import="com.soleexpressions.ecommercestore.POJOs.CustomizedShoe" %>

<%
    List<CustomizedShoe> basketItems = (List<CustomizedShoe>) request.getAttribute("basketItems");
    if (basketItems == null) {
        basketItems = new ArrayList<>();
    }

    double totalCost = 0.0;
    for (CustomizedShoe item : basketItems) {
        totalCost += item.getCalculatedTotalCost();
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Basket</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@200;300;400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/basket.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css"/>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/footer.css"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css" rel="stylesheet"/>
</head>
<body>
    <div class="desktop">
        <jsp:include page="/views/common/navbar.jsp"/>

        <div class="page-content-wrapper">
            <div class="basket-container">
                <h1>Your Shopping Basket</h1>

                <% if (basketItems.isEmpty()) { %>
                    <p class="empty-basket-message">Your basket is empty. Start customizing your shoes!</p>
                    <div class="empty-basket-actions">
                        <a href="<%= request.getContextPath() %>/customizations" class="btn btn-success">Customize a Shoe</a>
                    </div>
                <% } else { %>
                    <div class="basket-items">
                        <% for (CustomizedShoe item : basketItems) { %>
                            <div class="basket-item-card">
                                <div class="item-details">
                                    <h3>Custom <%= item.getShoeName() != null ? item.getShoeName() : "Shoe" %></h3>
                                    <p>Size: <%= item.getSelectedSize() %></p>
                                    <p>Base Color: <span class="color-swatch" style="background-color: <%= item.getSelectedBaseColor() %>; border: 1px solid <%= item.getSelectedBaseColor().equalsIgnoreCase("#FFFFFF") ? "#ccc" : item.getSelectedBaseColor() %>;"></span> <%= item.getSelectedBaseColor() %></p>
                                    <p>Sole Color: <span class="color-swatch" style="background-color: <%= item.getSelectedSoleColor() %>; border: 1px solid <%= item.getSelectedSoleColor().equalsIgnoreCase("#FFFFFF") ? "#ccc" : item.getSelectedSoleColor() %>;"></span> <%= item.getSelectedSoleColor() %></p>
                                    <p>Lace Color: <%= item.getSelectedLaceColor() %></p>
                                    <p>Artist: <%= item.getArtistId() == 0 ? "Not Assigned" : item.getArtistId() %></p>
                                    <p class="item-cost">Cost: $<%= String.format("%.2f", item.getCalculatedTotalCost()) %></p>
                                </div>
                                <div class="item-actions">
                                    <form action="<%= request.getContextPath() %>/basket" method="get">
                                        <input type="hidden" name="action" value="remove" />
                                        <input type="hidden" name="customizedShoeId" value="<%= item.getId() %>" />
                                        <button type="submit" class="btn btn-danger btn-sm">Remove</button>
                                    </form>
                                </div>
                            </div>
                        <% } %>
                    </div>

                    <div class="basket-summary">
                        <h2>Total: $<%= String.format("%.2f", totalCost) %></h2>
                        <div class="basket-actions">
                            <form action="<%= request.getContextPath() %>/basket" method="get" class="d-inline">
                                <input type="hidden" name="action" value="clear" />
                                <button type="submit" class="btn btn-outline-danger">Clear Basket</button>
                            </form>
                            <form action="<%= request.getContextPath() %>/basket" method="post" class="d-inline">
                                <input type="hidden" name="action" value="checkout" />
                                <button type="submit" class="btn btn-success">Proceed to Checkout</button>
                            </form>
                        </div>
                    </div>
                <% } %>
            </div>
        </div>
    </div>
    <jsp:include page="/views/common/footer.jsp"/>
    <%@ include file="/jspf/toastr-messages.jspf" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>