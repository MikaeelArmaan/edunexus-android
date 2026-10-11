package in.sbnexus.edunexus

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.SafeBrowsingResponse
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val loginUrl = "https://edu.sbnexus.in/login"
    private val allowedHost = "edu.sbnexus.in"
    private lateinit var root: LinearLayout
    private var webView: WebView? = null
    private var showingWeb = false

    private val ink = Color.rgb(21, 43, 35)
    private val muted = Color.rgb(101, 117, 109)
    private val green = Color.rgb(22, 130, 76)
    private val page = Color.rgb(248, 251, 249)
    private val line = Color.rgb(228, 236, 231)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = page
        window.navigationBarColor = Color.WHITE
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        showHome()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun rounded(color: Int, radiusDp: Int, strokeColor: Int? = null): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = dp(radiusDp).toFloat()
            if (strokeColor != null) setStroke(dp(1), strokeColor)
        }
    }

    private fun text(
        value: String,
        size: Float,
        color: Int = ink,
        bold: Boolean = false
    ): TextView = TextView(this).apply {
        text = value
        textSize = size
        setTextColor(color)
        if (bold) setTypeface(typeface, Typeface.BOLD)
        includeFontPadding = true
    }

    private fun baseColumn(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setBackgroundColor(page)
    }

    private fun addGap(parent: LinearLayout, height: Int) {
        parent.addView(View(this), LinearLayout.LayoutParams(1, dp(height)))
    }

    private fun addText(
        parent: LinearLayout,
        value: String,
        size: Float,
        color: Int = ink,
        bold: Boolean = false,
        top: Int = 0
    ): TextView {
        return text(value, size, color, bold).also {
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.topMargin = dp(top)
            parent.addView(it, params)
        }
    }

    private fun makeButton(label: String, primary: Boolean, onClick: () -> Unit): Button {
        return Button(this).apply {
            text = label
            textSize = 15f
            isAllCaps = false
            setTextColor(if (primary) Color.WHITE else green)
            background = rounded(if (primary) green else Color.WHITE, 14,
                if (primary) null else line)
            minHeight = dp(52)
            setPadding(dp(18), dp(8), dp(18), dp(8))
            setOnClickListener { onClick() }
        }
    }

    private fun showHome() {
        showingWeb = false
        webView?.let {
            (it.parent as? ViewGroup)?.removeView(it)
            it.destroy()
        }
        webView = null

        root = baseColumn()
        val scroll = ScrollView(this).apply {
            isFillViewport = true
            clipToPadding = false
        }
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(20), dp(22), dp(28))
        }

        val brandRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val mark = TextView(this).apply {
            text = "E"
            textSize = 24f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(green)
            gravity = Gravity.CENTER
            background = rounded(Color.rgb(229, 245, 233), 13)
        }
        brandRow.addView(mark, LinearLayout.LayoutParams(dp(46), dp(46)))
        val brandName = text("EduNexus", 22f, ink, true)
        val brandParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        brandParams.leftMargin = dp(12)
        brandRow.addView(brandName, brandParams)
        val signIn = TextView(this).apply {
            text = "Sign in ↗"
            textSize = 13f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(green)
            setPadding(dp(8), dp(12), dp(2), dp(12))
            setOnClickListener { showLogin() }
        }
        brandRow.addView(signIn)
        content.addView(brandRow)

        addGap(content, 28)
        val hero = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(26), dp(22), dp(26))
            background = rounded(Color.rgb(235, 247, 238), 22)
        }
        val eyebrow = text("SCHOOL MANAGEMENT, REIMAGINED", 10f, green, true)
        hero.addView(eyebrow)
        hero.addView(text("More time for teaching.\nLess time on paperwork.", 31f, ink, true),
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = dp(17)
            })
        hero.addView(text(
            "Bring your school's everyday operations into one organised digital experience with EduNexus.",
            15f, muted
        ), LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            topMargin = dp(14)
        })
        hero.addView(makeButton("Get started  →", true) { showLogin() },
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(54)).apply {
                topMargin = dp(23)
            })
        content.addView(hero)

        addGap(content, 30)
        addText(content, "Explore EduNexus", 24f, ink, true)
        addText(content, "A connected platform for everyday school operations.", 14f, muted, top = 7)

        val featureData = listOf(
            Triple("01  Student Management", "Organise student information and records.", Color.rgb(230, 247, 236)),
            Triple("02  Academics", "Bring classes, subjects and academic planning together.", Color.rgb(234, 242, 255)),
            Triple("03  Attendance", "Support daily attendance workflows.", Color.rgb(240, 234, 255)),
            Triple("04  Fees & Finance", "Organise fee-related and finance workflows.", Color.rgb(255, 244, 220)),
            Triple("05  Staff & Teachers", "Keep school staff information organised.", Color.rgb(227, 248, 250)),
            Triple("06  Reports & Insights", "Review school information more clearly.", Color.rgb(255, 235, 242))
        )
        featureData.forEach { (title, description, tint) ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(18), dp(17), dp(18), dp(17))
                background = rounded(Color.WHITE, 16, line)
            }
            card.addView(text(title, 16f, ink, true))
            card.addView(text(description, 13f, muted),
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    topMargin = dp(7)
                })
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.topMargin = dp(11)
            content.addView(card, params)
        }

        addGap(content, 25)
        val closing = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(22), dp(20), dp(22))
            background = rounded(Color.rgb(18, 60, 42), 18)
        }
        closing.addView(text("Ready to get started?", 22f, Color.WHITE, true))
        closing.addView(text("Sign in with your existing EduNexus account.", 14f, Color.rgb(205, 226, 213)),
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = dp(8)
            })
        closing.addView(makeButton("Open EduNexus  →", true) { showLogin() },
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(52)).apply {
                topMargin = dp(18)
            })
        content.addView(closing)
        addGap(content, 18)
        addText(content, "© ${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)} EduNexus", 12f, muted)

        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))
        setContentView(root)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showLogin() {
        showingWeb = true
        root = baseColumn()

        val toolbar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(12), dp(7), dp(12), dp(7))
            setBackgroundColor(Color.WHITE)
        }
        val homeButton = TextView(this).apply {
            text = "‹  Home"
            textSize = 15f
            setTextColor(green)
            setTypeface(typeface, Typeface.BOLD)
            setPadding(dp(8), dp(12), dp(16), dp(12))
            setOnClickListener { showHome() }
        }
        toolbar.addView(homeButton)
        toolbar.addView(text("EduNexus", 17f, ink, true),
            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        root.addView(toolbar, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))

        val web = WebView(this)
        webView = web
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.databaseEnabled = true
        web.settings.allowFileAccess = false
        web.settings.allowContentAccess = false
        web.settings.javaScriptCanOpenWindowsAutomatically = false
        web.settings.setSupportMultipleWindows(false)
        web.settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
        web.settings.safeBrowsingEnabled = true
        web.setDownloadListener { url, _, _, _, _ ->
            try {
                val uri = Uri.parse(url)
                if (uri.scheme == "https") startActivity(Intent(Intent.ACTION_VIEW, uri))
            } catch (_: Exception) {
                Toast.makeText(this, "Unable to open download.", Toast.LENGTH_SHORT).show()
            }
        }

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(web, false)

        web.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                // Keep the native toolbar branded as EduNexus.
            }
        }
        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url
                val scheme = uri.scheme ?: ""
                val host = uri.host?.lowercase() ?: ""

                if (scheme == "https" && (host == allowedHost || host.endsWith(".$allowedHost"))) {
                    return false
                }

                if (scheme == "http" || scheme == "https") {
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    } catch (_: Exception) {
                        Toast.makeText(this@MainActivity, "No app can open this link.", Toast.LENGTH_SHORT).show()
                    }
                    return true
                }

                // Do not allow intent:, file:, content:, or other untrusted schemes in WebView.
                return true
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: android.webkit.WebResourceError
            ) {
                if (request.isForMainFrame) {
                    Toast.makeText(this@MainActivity, "Unable to load EduNexus. Check your internet connection.", Toast.LENGTH_LONG).show()
                }
            }
        }

        root.addView(web, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
        ))
        setContentView(root)
        web.loadUrl(loginUrl)
    }

    override fun onBackPressed() {
        if (showingWeb) {
            val web = webView
            if (web != null && web.canGoBack()) {
                web.goBack()
            } else {
                showHome()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        webView?.apply {
            stopLoading()
            loadUrl("about:blank")
            removeAllViews()
            destroy()
        }
        webView = null
        super.onDestroy()
    }
}
