#!/usr/bin/env swift
// Generates the TapSleep iOS app icon (1024×1024 PNG).
// Run from the project root: swift scripts/generate_icon.swift

import AppKit
import CoreGraphics

let size = 1024
let s = CGFloat(size)
let outputRelative = "iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/AppIcon.png"

// ── Context ──────────────────────────────────────────────────────────────────
let colorSpace = CGColorSpaceCreateDeviceRGB()
guard let ctx = CGContext(
    data: nil,
    width: size,
    height: size,
    bitsPerComponent: 8,
    bytesPerRow: 0,
    space: colorSpace,
    bitmapInfo: CGImageAlphaInfo.premultipliedLast.rawValue
) else { print("Failed to create CGContext"); exit(1) }

func rgb(_ r: Int, _ g: Int, _ b: Int, _ a: CGFloat = 1) -> CGColor {
    CGColor(colorSpace: colorSpace,
            components: [CGFloat(r)/255, CGFloat(g)/255, CGFloat(b)/255, a])!
}

// ── Layer 1: Night background #0A0D1A ────────────────────────────────────────
ctx.setFillColor(rgb(0x0A, 0x0D, 0x1A))
ctx.fill(CGRect(x: 0, y: 0, width: s, height: s))

// ── Layer 2: Lavender radial glow (#9B8FC4 @ 15% alpha) ──────────────────────
// Centre: 40% width, 30% from top → in CGContext y-up: 70% from bottom
let gradCenter = CGPoint(x: 0.40 * s, y: 0.70 * s)
let gradRadius = 0.60 * s

let gradColors = [rgb(0x9B, 0x8F, 0xC4, 0.15), rgb(0x9B, 0x8F, 0xC4, 0.0)] as CFArray
let gradLocations: [CGFloat] = [0, 1]
guard let gradient = CGGradient(colorsSpace: colorSpace, colors: gradColors, locations: gradLocations)
else { print("Failed to create gradient"); exit(1) }

ctx.drawRadialGradient(gradient,
                       startCenter: gradCenter, startRadius: 0,
                       endCenter:   gradCenter, endRadius:   gradRadius,
                       options: [])

// ── Layer 3: MoonGlow inset strip — top 1% (#C8C0A8 @ 10%) ───────────────────
let stripH = 0.01 * s
ctx.setFillColor(rgb(0xC8, 0xC0, 0xA8, 0.10))
ctx.fill(CGRect(x: 0, y: s - stripH, width: s, height: stripH))

// ── Layer 4: Crescent moon (Lavender #9B8FC4) ─────────────────────────────────
// Original path lives in a 64×64 SVG viewport (y down).
// Scale = 16 → moon height 38*16 = 608 px (~59% of canvas).
// tx=56, ty=16 centres the bbox at (54.1%, 54.1%) of 1024.
// Coordinate conversion (SVG y-down → CGContext y-up):
//   cgX = tx + svgX * scale
//   cgY = s - ty - svgY * scale
let scale: CGFloat = 16
let tx:    CGFloat = 56
let ty:    CGFloat = 16

func pt(_ x: CGFloat, _ y: CGFloat) -> CGPoint {
    CGPoint(x: tx + x * scale, y: s - ty - y * scale)
}

let moon = CGMutablePath()
moon.move(to: pt(38, 12))
moon.addCurve(to: pt(19, 31), control1: pt(27.5, 12),  control2: pt(19,   20.5))
moon.addCurve(to: pt(38, 50), control1: pt(19,  41.5), control2: pt(27.5, 50))
moon.addCurve(to: pt(27, 31), control1: pt(32,  46),   control2: pt(27,   39))
moon.addCurve(to: pt(38, 12), control1: pt(27,  23),   control2: pt(32,   16))
moon.closeSubpath()

ctx.setFillColor(rgb(0x9B, 0x8F, 0xC4))
ctx.addPath(moon)
ctx.fillPath()

// ── Export PNG ────────────────────────────────────────────────────────────────
guard let cgImage = ctx.makeImage() else { print("makeImage failed"); exit(1) }

let rep = NSBitmapImageRep(cgImage: cgImage)
guard let pngData = rep.representation(using: .png, properties: [:])
else { print("PNG representation failed"); exit(1) }

let cwd = URL(fileURLWithPath: FileManager.default.currentDirectoryPath)
let outputURL = cwd.appendingPathComponent(outputRelative)

do {
    try FileManager.default.createDirectory(at: outputURL.deletingLastPathComponent(),
                                            withIntermediateDirectories: true)
    try pngData.write(to: outputURL)
    print("✓ Icon written to \(outputURL.path)")
} catch {
    print("Write failed: \(error)")
    exit(1)
}
