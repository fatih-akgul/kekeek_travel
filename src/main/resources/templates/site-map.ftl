<!DOCTYPE html>
<html lang="en">
<#include "/parts/html.head.ftl">

<body>
<main>
    <div class="main-content">
        <h1>${pageTitle}</h1>
        <div class="content">
        <#list articles as article>
            &nbsp; &nbsp; <strong>${article?counter}.</strong> <a
                href="/${article.identifier}">${article.description}</a> <br>
        </#list>
        </div>
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
