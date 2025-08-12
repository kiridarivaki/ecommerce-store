<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.soleexpressions.ecommercestore.POJOs.ArtistProfile" %>
<%@ page import="com.soleexpressions.ecommercestore.POJOs.User" %>
<%@ page import="com.soleexpressions.ecommercestore.DAOs.CustomizedShoeDAO" %>
<%@ page import="com.soleexpressions.ecommercestore.DAOs.CustomizedShoeDAOImpl" %>
<%@ page import="com.soleexpressions.ecommercestore.POJOs.CustomizedShoe" %>

<%
    List<ArtistProfile> artistProfiles = (List<ArtistProfile>) request.getAttribute("artistProfiles");
    if (artistProfiles == null) {
        artistProfiles = new ArrayList<>();
    }

    String customizationIdParam = request.getParameter("customizationId");
    int customizationId = Integer.parseInt(customizationIdParam);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Select Artist</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@200;300;400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/artist.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css"/>
</head>
<body>
    <div class="desktop">
        <jsp:include page="/views/common/navbar.jsp"/>

        <div class="page-content-wrapper">
            <div class="artist-selection-container">
                <h1>Select an Artist for Your Custom Shoe</h1>

                <form id="artistSelectionForm" action="${pageContext.request.contextPath}/artists" method="post">
                    <input type="hidden" name="customizationId" value="<%= customizationId %>" />
                    <input type="hidden" id="selectedArtistId" name="selectedArtistId" value="" />
                    <input type="hidden" name="action" value="updateArtist" />

                    <div class="artist-list">
                        <% if (artistProfiles.isEmpty()) { %>
                            <p class="no-artists-message">No artists available at the moment. Please check back later.</p>
                        <% } else { %>
                            <% for (ArtistProfile artistProfile : artistProfiles) {
                                   User user = artistProfile.getUser();
                                   if (user != null) {
                            %>
                                <div class="artist-card" data-artist-id="<%= user.getId() %>">
                                    <img src="${pageContext.request.contextPath}/img/account.svg" alt="Profile" class="artist-avatar">
                                    <div class="artist-details">
                                        <h3><%= user.getFirstname() %> <%= user.getLastname() %></h3>
                                        <p><%= artistProfile.getBio() %></p>
                                        <p>Style: <%= artistProfile.getStyle() %></p>
                                    </div>
                                    <div class="socials-wrapper">
                                        <% if (artistProfile.getInstagram() != null && !artistProfile.getInstagram().isEmpty()) { %>
                                            <a href="<%= artistProfile.getInstagram() %>" target="_blank" class="social-button">
                                                <img src="${pageContext.request.contextPath}/img/instagram.png" alt="Instagram">
                                            </a>
                                        <% } %>
                                        <% if (artistProfile.getFacebook() != null && !artistProfile.getFacebook().isEmpty()) { %>
                                            <a href="<%= artistProfile.getFacebook() %>" target="_blank" class="social-button">
                                                <img src="${pageContext.request.contextPath}/img/facebook.png" alt="Facebook">
                                            </a>
                                        <% } %>
                                        <% if (artistProfile.getPortfolioUrl() != null && !artistProfile.getPortfolioUrl().isEmpty()) { %>
                                            <a href="<%= artistProfile.getPortfolioUrl() %>" target="_blank" class="social-button">
                                                <img src="${pageContext.request.contextPath}/img/website.png" alt="Portfolio">
                                            </a>
                                        <% } %>
                                    </div>
                                    <button type="button" class="select-artist-button" data-artist-id="<%= user.getId() %>">Select Artist</button>
                                </div>
                            <%     }
                               }
                            %>
                        <% } %>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary" id="saveArtistBtn" disabled>Save Artist</button>
                    </div>
                </form>

                <div class="form-actions">
                    <form id="addToBasketForm" action="${pageContext.request.contextPath}/basket" method="post">
                        <input type="hidden" name="action" value="add" />
                        <input type="hidden" name="customizationId" value="<%= customizationId %>" />
                        <button type="submit" class="btn btn-success" id="addToBasketBtn" disabled>Add to Basket</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="/views/common/footer.jsp"/>
    <%@ include file="/jspf/toastr-messages.jspf" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/artist.js"></script>
</body>
</html>