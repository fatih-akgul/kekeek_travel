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
        <h1>${articlePage.title}</h1>
        <#if (articlePage.imageDescription)?has_content>
            <img src="${articleImage}" alt="${articlePage.imageDescription}">
            <div class="picture_caption">
                ${articlePage.imageDescription}
            </div>
            <br><br>
        </#if>

        <p>
            ${mainContent}
        </p>


        <div class="child-pages">
            <#if children?has_content >
                <#if article.identifier == articlePage.identifier>
                    ${article.title}
                <#else>
                    <a href="/${article.identifier}">${article.title}</a>
                </#if>
                &nbsp;<span class="highlight">|</span>&nbsp;
            </#if>
            <#list children as child>
                <#if child.identifier == articlePage.identifier>
                    ${child.title}
                <#else>
                    <a href="/${child.identifier}">${child.title}</a>
                </#if>
                <#sep>&nbsp;<span class="highlight">|</span>&nbsp;</#sep>
            </#list>
        </div>

        <#if children?has_content && parent?has_content>
            <hr>
        </#if>
        <div class="breadcrumbs-bottom">
            <#if parent?has_content>
                <a href="${parent.identifier}">${parent.title}</a>
            </#if>
            <#if siblings?has_content>
                &nbsp;<span class="highlight">|</span>&nbsp;
                <#list siblings as sibling>
                    <#if sibling.identifier == article.identifier>
                        ${sibling.title}
                    <#else>
                        <a href="/${sibling.identifier}">${sibling.title}</a>
                    </#if>
                    <#sep>&nbsp;<span class="highlight">|</span>&nbsp;</#sep>
                </#list>
            </#if>
        </div>
        <#if children?has_content && parent?has_content>
            <hr>
        </#if>

        <br>
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
