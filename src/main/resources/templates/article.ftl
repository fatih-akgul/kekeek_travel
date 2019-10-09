<!DOCTYPE html>
<html lang="en">
<#include "/parts/html.head.ftl">

<body>
<main>
    <div class="breadcrumbs-top">
        <#include "/parts/breadcrumbs.ftl">
    </div>
    <div class="main-content">
        <!-- google_ad_section_start -->
        <h1>${articlePage.description}</h1>
        <#if (articlePage.imageDescription)?has_content>
            <img src="${articleImage}" alt="${articlePage.imageDescription}">
            <div class="picture_caption">
                ${articlePage.imageDescription}
            </div>
            <br>
        </#if>

        <div class="content">
            ${mainContent}
        </div>

        <#if childPages?has_content >
        <div class="child-pages">

            <#if article.identifier == articlePage.identifier>
                ${article.title}
            <#else>
                <a href="/${article.identifier}">${article.title}</a>
            </#if>
            &nbsp;<span class="highlight">|</span>&nbsp;

            <#list childPages as childPage>
                <#if childPage.identifier == articlePage.identifier>
                    ${childPage.title}
                <#else>
                    <a href="/${childPage.identifier}">${childPage.title}</a>
                </#if>
                <#sep>&nbsp;<span class="highlight">|</span>&nbsp;</#sep>
            </#list>
        </div>
        </#if>

        <!-- google_ad_section_end -->

        <#if childArticles?has_content>
        <div class="child-articles">
            <#if childPages?has_content>
                <br><h2>In ${article.title}...</h2>
            </#if>
            <#list childArticles as child>
                <#if child.identifier == articlePage.identifier>
                    ${child.title}
                <#else>
                    <a href="/${child.identifier}">${child.title}</a>
                </#if>
                <#sep>&nbsp;<span class="highlight">--</span>&nbsp;</#sep>
            </#list>
        </div>
        </#if>

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
