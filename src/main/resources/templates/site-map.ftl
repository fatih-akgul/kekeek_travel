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
    <h1>${pageTitle}</h1>
    <#list articles as article>
      &nbsp; &nbsp; <strong>${article?index + 1}.</strong> <a href="/${article.identifier}">${article.description}</a> <br>
    </#list>
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
