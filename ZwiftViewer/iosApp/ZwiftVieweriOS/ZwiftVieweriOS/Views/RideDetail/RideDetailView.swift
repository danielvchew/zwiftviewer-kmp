//
//  RideDetailView.swift
//  ZwiftVieweriOS
//
//  Created by Daniel Chew on 6/18/25.
//

import SwiftUI
import Shared // for Ride
import os

private let logger = Logger(subsystem: "com.danielchew.zwiftviewer", category: "ZwiftDebug")

struct RideDetailView: View {
    let ride: Ride
    let onBack: (() -> Void)?

    var body: some View {
        ScrollViewReader { proxy in
            ScrollView {
                VStack(alignment: .leading, spacing: 12) {
                    Color.clear.frame(height: 1).id("top") // anchor

                    if let onBack = onBack {
                        Button(action: onBack) {
                            Label("Back", systemImage: "chevron.left")
                                .foregroundColor(.blue)
                                .padding(.bottom)
                        }
                    }
                    if let date = ride.date?.label() {
                        Text(date).font(.headline)
                    }

                    if let title = ride.title {
                        Text(title).font(.title2).bold()
                    }

                    if let distance = ride.distance {
                        Text("Distance: \(String(format: "%.1f", Double(truncating: distance) / 1000)) km")
                    }

                    if let elevation = ride.elevation {
                        Text("Elevation: \(Int(truncating: elevation)) ft")
                    }

                    if let time = ride.elapsed?.label() {
                        Text(time)
                    }

                    if let speed = ride.avgSpeed {
                        Text(String(format: "Avg Speed: %.2f km/h", Double(truncating: speed)))
                    }

                    if let hr = ride.avgHr?.label() {
                        Text("Avg HR: \(hr) bpm")
                    }

                    if let maxHr = ride.maxHr?.label() {
                        Text("Max HR: \(maxHr) bpm")
                    }

                    if let cadence = ride.avgCadence?.label() {
                        Text("Avg Cadence: \(cadence) rpm")
                    }

                    if let calories = ride.calories {
                        Text("Calories: \(Int(truncating: calories)) kcal")
                    }

                    if let sport = ride.sport {
                        Text("Sport: \(sport)")
                    }

                    Spacer().frame(minHeight: 1)
                }
                .padding()
                .frame(minHeight: UIScreen.main.bounds.height * 1.1)
            }
            .scrollBounceBehavior(.basedOnSize)
            .id(ride.zaid ?? UUID().uuidString)
            .navigationTitle("Ride Details")
            .navigationBarTitleDisplayMode(.inline)
            .onAppear {
                proxy.scrollTo("top", anchor: .top)
                logger.info("RideDetailView loaded for ride ID: \(ride.zaid ?? "unknown")")
            }
        }
    }
}
