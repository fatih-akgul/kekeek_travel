<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="keywords" content="${keywords}">
    <meta name="description" content="${description}">
    <title>${pageTitle}</title>
    <link href="/css/main.css" rel="stylesheet">
</head>

<body>
<main>
    <div class="main-content">
        <h1>Contact Us</h1>
        <#if contactFormData.errors?has_content>
            <#list contactFormData.errors as error>
                <div class="error">* ${error}</div>
            </#list>
        <#elseif contactFormData.messages?has_content>
            <#list contactFormData.messages as message>
                <div class="message">${message}</div>
            </#list>
        </#if>
        <form method="post" action="/contact">
            <label for="contact-name">Name: &nbsp; </label><input type="text" name="name" id="contact-name" size="40" value="${contactFormData.name}" /> <br>
            <label for="contact-email">Email: &nbsp;&nbsp; </label><input type="text" name="email" id="contact-email" size="40" value="${contactFormData.email}" /> <br>
            <label for="contact-subject">Subject: </label><input type="text" name="subject" id="contact-subject" size="40" value="${contactFormData.subject}" /> <br>
            <br>
            <label for="contact-message">Message: </label><br><textarea name="message" id="contact-message" rows="9" cols="75">${contactFormData.message}</textarea> <br>
            <input type="submit" value="Send">
        </form>
    </div>
</main>
<header>
    <div class="main-nav">
        <#include "/parts/top-nav.ftl">
    </div>
</header>
<aside>
    <div class="sidebar">
        <#include "/parts/sidebar.ftl">
    </div>
</aside>
<footer>
    <div class="footer">
        <#include "/parts/footer.ftl">
    </div>
</footer>
<script src="/js/main.js"></script>
</body>

</html>
