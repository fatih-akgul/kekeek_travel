yieldUnescaped '<!DOCTYPE html>'
html(lang:'en') {
    head {
        meta('http-equiv':'"Content-Type" content="text/html; charset=utf-8"')
        title('My page')
    }
    body {
        h2 ('Inside Directory')
        div ("msg: $msg")
        div ("time: $time")
    }
}
