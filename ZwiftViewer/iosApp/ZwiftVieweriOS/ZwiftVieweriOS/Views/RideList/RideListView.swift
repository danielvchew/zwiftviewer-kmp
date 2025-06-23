func formatRideDate(_ ride: Ride) -> String {
    guard let dateWrapper = ride.date,
          let epochSeconds = dateWrapper.toEpochSecondsOrNull() else {
        return "Unknown Date"
    }
    
    let date = Date(timeIntervalSince1970: TimeInterval(truncating: epochSeconds))
    let formatter = DateFormatter()
    formatter.dateFormat = "MMM d, yyyy"
    return formatter.string(from: date)
}

func rideStatsText(for ride: Ride) -> (String, String, String) {
    var distanceText = ""
    var elevationText = ""
    var elapsedText = ""

    if let distance = ride.distance {
        let miles = Double(truncating: distance) * 0.000621371
        distanceText = String(format: "%.1f mi", miles)
    }

    if let elevation = ride.elevation {
        let feet = Double(truncating: elevation) * 3.28084
        elevationText = String(format: "%.0f ft", feet)
    }

    if let elapsedWrapper = ride.elapsed,
       let totalSeconds = elapsedWrapper.doubleValue {
        let hours = Int(truncating: totalSeconds) / 3600
        let minutes = (Int(truncating: totalSeconds) % 3600) / 60
        elapsedText = hours > 0 ? "\(hours)h \(minutes)m" : "\(minutes)m"
    }

    return (distanceText, elevationText, elapsedText)
}
//
//  RideListView.swift
//  ZwiftVieweriOS
//
//  Created by Daniel Chew on 6/17/25.
//

import SwiftUI
import Shared
import os

private let logger = Logger(subsystem: "com.danielchew.zwiftviewer", category: "ZwiftDebug")

struct RideListView: View {
    let cookies: [HTTPCookie]
    let onSelect: (Ride) -> Void
    let onBack: (() -> Void)?

    @State private var rides: [Ride] = []
    @State private var isLoading: Bool = true
    @State private var error: String?

    var body: some View {
        List {
            if let onBack = onBack {
                Button(action: onBack) {
                    Label("Back", systemImage: "chevron.left")
                        .foregroundColor(.blue)
                        .padding(.bottom)
                }
            }
            Text("Ride List")
                .font(.title2)
                .bold()
                .padding(.bottom, 8)

            if isLoading {
                ProgressView("Loading rides...")
            } else if let error = error {
                Text("Error: \(error)")
                    .foregroundColor(.red)
            } else {
                ForEach(rides, id: \.zaid) { ride in
                    let dateText = formatRideDate(ride)
                    let (distanceText, elevationText, elapsedText) = rideStatsText(for: ride)

                    Button(action: {
                        onSelect(ride)
                    }) {
                        VStack(alignment: .leading, spacing: 6) {
                            Text(dateText)
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                            if let title = ride.title {
                                Text(title)
                                    .font(.headline)
                            }
                            HStack(spacing: 0) {
                                Text(distanceText)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                Text(elevationText)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                Text(elapsedText)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                            }
                            .font(.subheadline)
                            .foregroundColor(.gray)
                        }
                        .padding(.vertical, 4)
                    }
                }
            }
        }
        .listStyle(PlainListStyle())
        .padding()
        .onAppear {
            logger.info("RideListView: onAppear triggered")
            Task {
                logger.info("RideListView: loading rides with cookies: \(String(describing: cookies.map { "\($0.name)=\($0.value)" }))")
                do {
                    let cookieMap = Dictionary(uniqueKeysWithValues: cookies.map { ($0.name, $0.value) })
                    logger.info("RideListView: parsed cookieMap keys: \(String(describing: cookieMap.keys))")
                    logger.info("RideListView: profileUrl from cookieMap = \(String(describing: cookieMap["profileUrl"]))")
                    guard let profileUrl = cookieMap["profileUrl"], !profileUrl.isEmpty else {
                        logger.error("RideListView: profileUrl missing from cookieMap — aborting ride fetch.")
                        self.error = "Missing profile URL"
                        return
                    }

                    logger.info("RideListView: verifying profileUrl before extracting Zwift ID = \(profileUrl)")
                    let store = IOSCookieStore()
                    logger.info("RideListView: calling legacyGetZwiftId()")
                    guard let zwiftId = try await store.legacyGetZwiftId(), !zwiftId.isEmpty else {
                        logger.error("RideListView: zwiftId is nil or empty — aborting ride fetch.")
                        self.error = "Unable to retrieve Zwift ID"
                        return
                    }
                    logger.info("RideListView: Successfully extracted zwiftId = \(zwiftId)")
                    let cookieHeader = cookieMap.map { "\($0.key)=\($0.value)" }.joined(separator: "; ")
                    logger.info("RideListView: calling getUserRideHistory with zwiftId = \(zwiftId)")
                    let realRides = try await ZwiftPowerRideFetcher.shared.getUserRideHistory(
                        zwiftId: zwiftId,
                        cookieHeader: cookieHeader
                    )
                    // let realRides = try await ZwiftPowerRideFetcher.shared.getUserRideHistory(profileId: zwiftId, cookies: cookieMap)
                    logger.info("RideListView: fetched \(realRides.count) rides")
                    self.rides = realRides
                    self.isLoading = false
                    logger.info("RideListView: successfully loaded \(realRides.count) rides")
                } catch {
                    logger.error("RideListView: error fetching rides - \(String(describing: error.localizedDescription))")
                    self.error = error.localizedDescription
                    self.isLoading = false
                }
            }
        }
    }
}
