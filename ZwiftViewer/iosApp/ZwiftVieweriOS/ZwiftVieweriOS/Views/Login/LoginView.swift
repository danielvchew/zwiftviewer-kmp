//
//  LoginView.swift
//  Loads ZwiftPower login via WebView and extracts cookies post-authentication
//

import SwiftUI
import Shared
import WebKit
import os

private let logger = Logger(subsystem: "com.danielchew.zwiftviewer", category: "ZwiftDebug")

struct LoginView: UIViewRepresentable {
    let onCookiesExtracted: ([String: String]) -> Void

    func makeUIView(context: Context) -> WKWebView {
        let webView = WKWebView()
        webView.navigationDelegate = context.coordinator

        let loginURL = URL(string: "https://zwiftpower.com/profile.php")!
        logger.debug("Loading login URL: \(loginURL.absoluteString)")
        webView.load(URLRequest(url: loginURL))

        return webView
    }

    func updateUIView(_ uiView: WKWebView, context: Context) {}

    func makeCoordinator() -> Coordinator {
        Coordinator(onCookiesExtracted: onCookiesExtracted)
    }

    class Coordinator: NSObject, WKNavigationDelegate {
        let onCookiesExtracted: ([String: String]) -> Void

        init(onCookiesExtracted: @escaping ([String: String]) -> Void) {
            self.onCookiesExtracted = onCookiesExtracted
        }

        func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
            if let finalURL = webView.url?.absoluteString {
                logger.debug("ZwiftDebug: LoginView : Final webView URL after page load = \(finalURL)")
            }

            webView.configuration.websiteDataStore.httpCookieStore.getAllCookies { cookies in
                let zwiftCookies = cookies.filter { $0.domain.contains("zwiftpower.com") }
                var cookieMap = Dictionary(uniqueKeysWithValues: zwiftCookies.map { ($0.name, $0.value) })

                logger.debug("ZwiftDebug: LoginView : Retrieved cookies after login: \(cookieMap)")

                guard cookieMap.keys.contains("phpbb3_lswlk_u") else {
                    logger.debug("Login not detected yet")
                    return
                }

                logger.debug("ZwiftDebug: LoginView : Detected successful login, evaluating DOM for profile URL")

                let script = """
                    var anchor = document.querySelector('a[href*="profile.php?z="]');
                    anchor ? anchor.href : "";
                """
                webView.evaluateJavaScript(script) { result, error in
                    if let profileUrl = result as? String, !profileUrl.isEmpty {
                        logger.debug("ZwiftDebug: LoginView : Extracted ZwiftPower profile URL from DOM: \(profileUrl)")
                        cookieMap["profileUrl"] = profileUrl
                    } else {
                        logger.debug("ZwiftDebug: LoginView : JS result did not contain Zwift ID URL")
                        if let error = error {
                            logger.debug("ZwiftDebug: LoginView : JavaScript evaluation error: \(error.localizedDescription)")
                        }
                    }

                    logger.debug("ZwiftDebug: LoginView : Saving full cookie map to store")
                    logger.debug("ZwiftDebug: LoginView : Full cookie map saved = \(cookieMap)")
                    IOSCookieStore().save(cookies: cookieMap)
                    self.onCookiesExtracted(cookieMap)
                }
            }
        }

        func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
            if let url = navigationAction.request.url {
                let urlString = url.absoluteString
                logger.debug("ZwiftDebug: LoginView : Captured navigation URL = \(urlString)")
            }
            decisionHandler(.allow)
        }
    }
}
