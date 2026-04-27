$files = Get-ChildItem -Path "C:\Users\ADMIN\Downloads\mbbSystem\src\main\resources\static" -Recurse -Filter *.html
foreach ($f in $files) {
    $content = Get-Content $f.FullName -Raw -Encoding UTF8
    if ($content -notmatch '<meta charset="UTF-8">') {
        $content = $content -replace '<head>', "<head>`n    <meta charset=`"UTF-8`">"
        Set-Content -Path $f.FullName -Value $content -Encoding UTF8
    }
}
