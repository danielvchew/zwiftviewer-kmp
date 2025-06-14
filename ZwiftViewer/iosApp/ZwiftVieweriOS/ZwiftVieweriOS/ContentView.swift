//
//  ContentView.swift
//  ZwiftVieweriOS
//
//  Created by Daniel Chew on 6/14/25.
//

import SwiftUI
import Shared

struct ContentView: View {
    var body: some View {
        VStack {
            Text("From Kotlin: \(Greeting().greet())")
                }
        .padding()
    }
}

#Preview {
    ContentView()
}
