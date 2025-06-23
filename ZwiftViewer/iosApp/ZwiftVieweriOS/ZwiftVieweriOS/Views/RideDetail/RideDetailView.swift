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

                    if let title = ride.title {
                        Text(title)
                            .font(.title3)
                            .bold()
                            .padding(.bottom, 8)
                    }

                    let dateString: String? = {
                        if let epochSeconds = ride.date?.toEpochSecondsOrNull() {
                            let timeInterval = TimeInterval(truncating: epochSeconds)
                            let date = Date(timeIntervalSince1970: timeInterval)
                            let formatter = DateFormatter()
                            formatter.dateFormat = "MMM dd, yyyy"
                            return formatter.string(from: date)
                        }
                        return nil
                    }()

                    if let dateString = dateString {
                        Text("📅 Date: \(dateString)")
                    }

                    if let sport = ride.sport {
                        Text("🏃 Sport: \(sport)")
                    }

                    if let time = ride.elapsed?.label() {
                        Text("🕒 Elapsed: \(time)")
                    }

                    if let distance = ride.distance?.doubleValue {
                        Text("📏 Distance: \(String(format: "%.1f", distance / 1000)) km")
                    }

                    if let elevation = ride.elevation?.doubleValue {
                        let elevationFeet = elevation * 3.28084
                        Text("🧗 Elevation: \(String(format: "%.0f", elevationFeet)) ft")
                    }

                    if let distanceMeters = ride.distance?.doubleValue,
                       let elapsedSeconds = ride.elapsed?.secondsValue?.doubleValue,
                       elapsedSeconds > 0 {
                        let miles = distanceMeters * 0.000621371
                        let hours = elapsedSeconds / 3600
                        let mph = miles / hours
                        Text("🚴 Avg Speed: \(String(format: "%.1f", mph)) mi/h")
                    }

                    if let hr = ride.avgHr?.label() {
                        Text("💓 Avg HR: \(hr) bpm")
                    }

                    if let maxHr = ride.maxHr?.label() {
                        Text("🔺 Max HR: \(maxHr) bpm")
                    }

                    if let power = ride.avgPower?.doubleValue?.doubleValue, power > 0 {
                        Text("⚡ Avg Power: \(String(format: "%.0f", power)) W")
                    }

                    if let cadence = ride.avgCadence?.label() {
                        Text("🔁 Avg Cadence: \(cadence) rpm")
                    }

                    if let calories = ride.calories?.doubleValue {
                        Text("🔥 Calories: \(String(format: "%.0f", calories)) kcal")
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
