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
  <div class="breadcrumbs-top">
      <#include "/parts/breadcrumbs.ftl">
  </div>
  <div class="main-content">
    <h1>${article.title}</h1>
      <#if (article.imageDescription)?has_content>
        <img src="${articleImage}" alt="${article.imageDescription}">
        <div class="picture_caption">
            ${article.imageDescription}
        </div>
        <br><br>
      </#if>

    <p>
        ${mainContent}
    </p>

    <hr>
    <div class="breadcrumbs-bottom">
        <#include "/parts/breadcrumbs.ftl">
    </div>
    <hr>
    <br>

    <div class="child-pages">
        <#list children as child>
          <a href="/${child.identifier}">${child.title}</a> <#sep><span class="highlight">|</span></#sep>
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
