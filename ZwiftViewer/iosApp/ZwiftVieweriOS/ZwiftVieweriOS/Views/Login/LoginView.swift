//
//  ZwiftPowerLoginView.swift
//  ZwiftVieweriOS
//
//  Created by Daniel Chew on 6/17/25.
//

import SwiftUI
import WebKit

struct LoginView: UIViewRepresentable {
    let onCookiesExtracted: ([String: String]) -> Void

    func makeUIView(context: Context) -> WKWebView {
        let webView = WKWebView()
        webView.navigationDelegate = context.coordinator
        if let url = URL(string: "https://zwiftpower.com") {
            let loginURL = URL(string: "https://zwiftpower.com/auth/login")!
            webView.load(URLRequest(url: loginURL))
        }
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
            webView.configuration.websiteDataStore.httpCookieStore.getAllCookies { cookies in
                let zwiftCookies = cookies.filter {
                    $0.domain.contains("zwiftpower.com")
                }

                let cookieMap = Dictionary(uniqueKeysWithValues: zwiftCookies.map { ($0.name, $0.value) })

                print("All cookies after login: \(cookieMap)")

                if cookieMap.keys.contains("phpbb3_lswlk_u") {
                    print("Detected login, sending cookies to app")
                    self.onCookiesExtracted(cookieMap)
                } else {
                    print("Login not detected yet")
                }
            }
        }
    }
}
