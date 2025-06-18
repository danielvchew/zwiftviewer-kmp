//
//  ZwiftPowerRideViewModel.swift
//  ZwiftVieweriOS
//
//  Created by Daniel Chew on 6/17/25.
//

import Shared

struct ZwiftPowerRideViewModel: Identifiable {
    private let model: ZwiftPowerRide

    init(_ model: ZwiftPowerRide) {
        self.model = model
        self.zid = model.zid
        self.title = model.title
        self.distance = Double(model.distance) / 1000.0
        self.avgPower = model.avgPower.count > 0 ? (model.avgPower[0] as? Int ?? 0) : 0
    }

    var id: String { zid }

    let zid: String
    let title: String
    let distance: Double
    let avgPower: Int
}
