<!DOCTYPE html>
<html lang="en">
<#include "/parts/html.head.ftl">

<body>
<main>
    <div class="main-content">
        ${mainContent}
        <hr>
        <br>
        ${faq}
        <br>
        ${latestArticles}
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
