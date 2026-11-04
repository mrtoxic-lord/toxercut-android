package io.github.artcraftandroid.filmcraft.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

private val CosmicPurple = Color(0xFF8B5CF6)
private val CosmicViolet = Color(0xFF6D45D9)
private val CosmicBlue = Color(0xFF3978D8)
private val CosmicGlow = Color(0xFFB49AFF)
private val CosmicDeep = Color(0xFF111126)
private val CosmicSurface = Color(0xFF19172F)
private val CosmicBorder = Color(0xFF39305F)

private val CosmicGradient = Brush.linearGradient(
    colors = listOf(
        CosmicPurple,
        CosmicViolet,
        CosmicBlue,
    ),
)

private val CosmicPanelGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF201A3B),
        Color(0xFF15152C),
        Color(0xFF101020),
    ),
)

/** A round icon button (top bar, transport row): 40 dp target, 24 dp icon. */
@Composable
fun FcIconButton(
    glyph: Glyph,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = FcSize.TopButton,
    iconSize: Dp = 24.dp,
    tint: Color = Fc.Icon,
) {
    Box(
        modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    listOf(
                        CosmicPurple.copy(alpha = 0.15f),
                        CosmicBlue.copy(alpha = 0.10f),
                    ),
                ),
            )
            .border(1.dp, CosmicBorder.copy(alpha = 0.65f), CircleShape)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        GlyphIcon(glyph, if (enabled) tint else Fc.Disabled, size = iconSize)
    }
}

/** A cosmic tool tile on the bottom toolbar. */
@Composable
fun ToolTile(
    glyph: Glyph,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    active: Boolean = false,
    tag: String? = null,
) {
    Column(
        modifier
            .size(FcSize.ToolTile)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (active) CosmicPurple.copy(alpha = 0.12f)
                else Color.Transparent,
            )
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .then(if (tag != null) Modifier.testTag(tag) else Modifier)
            .semantics { selected = active },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Box(
            Modifier
                .size(FcSize.ToolChip)
                .clip(RoundedCornerShape(9.dp))
                .then(
                    if (active) {
                        Modifier.background(CosmicGradient)
                    } else {
                        Modifier.background(
                            Brush.linearGradient(
                                listOf(
                                    CosmicPurple.copy(alpha = 0.16f),
                                    CosmicBlue.copy(alpha = 0.10f),
                                ),
                            ),
                        )
                    },
                )
                .then(
                    if (active) Modifier.border(
                        1.dp,
                        CosmicGlow.copy(alpha = 0.8f),
                        RoundedCornerShape(9.dp),
                    ) else Modifier,
                ),
            contentAlignment = Alignment.Center,
        ) {
            GlyphIcon(
                glyph,
                if (!enabled) Fc.Disabled
                else if (active) Color.White
                else Fc.Icon,
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            label,
            style = FcType.ToolLabel,
            color = if (enabled) {
                if (active) CosmicGlow else Fc.Body
            } else Fc.Disabled,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/** A small pill action on a panel header. */
@Composable
fun PanelAction(
    glyph: Glyph,
    description: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    tag: String? = null,
) {
    Box(
        Modifier
            .size(width = 40.dp, height = 32.dp)
            .clip(CircleShape)
            .then(
                if (enabled) Modifier.background(
                    Brush.linearGradient(
                        listOf(
                            CosmicPurple.copy(alpha = 0.22f),
                            CosmicBlue.copy(alpha = 0.16f),
                        ),
                    ),
                ) else Modifier,
            )
            .border(
                1.dp,
                if (enabled) CosmicBorder else Color.Transparent,
                CircleShape,
            )
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .then(if (tag != null) Modifier.testTag(tag) else Modifier)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        GlyphIcon(glyph, if (enabled) CosmicGlow else Fc.Disabled, size = 20.dp)
    }
}

/** Cosmic tool panel with a subtle space gradient. */
@Composable
fun ToolPanel(
    title: String?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    actions: (@Composable RowScope.() -> Unit)? = null,
    tag: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val close by rememberUpdatedState(onClose)
    var drag by remember { mutableFloatStateOf(0f) }

    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = FcSize.PanelInset)
            .clip(RoundedCornerShape(topStart = FcSize.PanelCorner, topEnd = FcSize.PanelCorner))
            .background(CosmicPanelGradient)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        CosmicPurple.copy(alpha = 0.55f),
                        CosmicBlue.copy(alpha = 0.12f),
                    ),
                ),
                shape = RoundedCornerShape(topStart = FcSize.PanelCorner, topEnd = FcSize.PanelCorner),
            )
            .then(if (tag != null) Modifier.testTag(tag) else Modifier),
    ) {
        Column(
            Modifier.fillMaxWidth().pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = { drag = 0f },
                    onDragEnd = { if (drag > 48.dp.toPx()) close() },
                    onVerticalDrag = { _, dy -> drag += dy },
                )
            },
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .semantics { contentDescription = "Close panel" }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { close() },
                    ),
                contentAlignment = Alignment.TopCenter,
            ) {
                Box(
                    Modifier
                        .padding(top = 16.dp)
                        .size(width = 36.dp, height = 4.dp)
                        .clip(CircleShape)
                        .background(CosmicGlow.copy(alpha = 0.7f)),
                )
            }

            if (title != null) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            start = FcSize.PanelPadding,
                            end = FcSize.PanelPadding,
                            bottom = 8.dp,
                        )
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        title,
                        style = FcType.PanelTitle,
                        color = CosmicGlow,
                        modifier = Modifier
                            .weight(1f)
                            .offset(y = if (actions == null) (-4.5).dp else 0.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    if (actions != null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            content = actions,
                        )
                    }
                }
            }
        }

        Column(
            Modifier
                .weight(1f, fill = false)
                .verticalScroll(rememberScrollState()),
        ) {
            content()
        }

        Spacer(
            Modifier
                .fillMaxWidth()
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .background(Color(0xFF0B0B19)),
        )
    }
}

/** How a slider's track is drawn. */
enum class SliderLook { Standard, Opacity }

/** Cosmic slider with a purple-blue gradient track. */
@Composable
fun FcSlider(
    fraction: Float,
    onChange: (Float) -> Unit,
    onDone: () -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    look: SliderLook = SliderLook.Standard,
    ticks: List<Float> = emptyList(),
    snapTo: Float? = null,
    description: String = "",
    tag: String? = null,
) {
    val change by rememberUpdatedState(onChange)
    val done by rememberUpdatedState(onDone)
    val haptic = LocalHapticFeedback.current
    var snapped by remember { mutableStateOf(false) }

    Canvas(
        modifier
            .fillMaxWidth()
            .height(40.dp)
            .then(if (tag != null) Modifier.testTag(tag) else Modifier)
            .semantics {
                contentDescription = description
                progressBarRangeInfo = ProgressBarRangeInfo(
                    fraction.coerceIn(0f, 1f),
                    0f..1f,
                )
                if (enabled) {
                    setProgress { v ->
                        change(v.coerceIn(0f, 1f))
                        done()
                        true
                    }
                }
            }
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput

                val pad = 10.dp.toPx()

                fun at(x: Float): Float {
                    val f = (
                        (x - pad) /
                            (size.width - 2 * pad).coerceAtLeast(1f)
                        ).coerceIn(0f, 1f)

                    val s = snapTo

                    if (s != null && kotlin.math.abs(f - s) < 0.015f) {
                        if (!snapped) {
                            haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                        }
                        snapped = true
                        return s
                    }

                    snapped = false
                    return f
                }

                awaitEachGesture {
                    val down = awaitFirstDown()
                    change(at(down.position.x))

                    horizontalDrag(down.id) { c ->
                        c.consume()
                        change(at(c.position.x))
                    }

                    done()
                }
            },
    ) {
        val pad = 10.dp.toPx()
        val y = size.height / 2f
        val track = 4.dp.toPx()
        val w = size.width - 2 * pad
        val x = pad + w * fraction.coerceIn(0f, 1f)
        val r = CornerRadius(2.dp.toPx())

        when (look) {
            SliderLook.Standard -> {
                drawRoundRect(
                    if (enabled) Fc.SliderOff else Fc.SliderDisabled,
                    Offset(pad, y - track / 2),
                    Size(w, track),
                    r,
                )

                if (enabled) {
                    drawRoundRect(
                        Brush.horizontalGradient(
                            listOf(CosmicPurple, CosmicBlue),
                            startX = pad,
                            endX = pad + w,
                        ),
                        Offset(pad, y - track / 2),
                        Size(x - pad, track),
                        r,
                    )
                }

                for (t in ticks) {
                    val tx = pad + w * t
                    drawRect(
                        if (x >= tx) CosmicGlow else Fc.SliderOff,
                        Offset(tx - 0.5.dp.toPx(), y - track / 2),
                        Size(1.dp.toPx(), track),
                    )
                }

                drawCircle(CosmicDeep, 11.dp.toPx(), Offset(x, y))
                drawCircle(
                    if (enabled) CosmicGlow else Fc.SliderOff,
                    9.dp.toPx(),
                    Offset(x, y),
                    style = Stroke(2.dp.toPx()),
                )
            }

            SliderLook.Opacity -> {
                drawRoundRect(
                    Brush.horizontalGradient(
                        listOf(Color.Black, CosmicPurple, Fc.Title),
                        startX = pad,
                        endX = pad + w,
                    ),
                    Offset(pad, y - track / 2),
                    Size(w, track),
                    r,
                )

                drawCircle(CosmicDeep, 11.dp.toPx(), Offset(x, y))
                drawCircle(
                    CosmicGlow,
                    9.dp.toPx(),
                    Offset(x, y),
                    style = Stroke(2.dp.toPx()),
                )
            }
        }
    }
}

/** A selectable chip for export settings. */
@Composable
fun SelectChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    tag: String? = null,
) {
    Row(
        Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (selected) {
                    Modifier.background(CosmicGradient)
                } else {
                    Modifier
                        .background(CosmicSurface)
                        .border(1.dp, CosmicBorder, RoundedCornerShape(12.dp))
                },
            )
            .clickable(role = Role.RadioButton, onClick = onClick)
            .then(if (tag != null) Modifier.testTag(tag) else Modifier)
            .semantics { this.selected = selected }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (selected) {
            GlyphIcon(LucideIcons.Check.glyph(), Color.White, size = 18.dp)
            Spacer(Modifier.width(8.dp))
        }

        Text(
            label,
            style = FcType.Label.copy(fontSize = 14.sp),
            color = if (selected) Color.White else Fc.Body,
        )
    }
}

/** A target chip with a check when chosen. */
@Composable
fun ToggleChip(label: String, selected: Boolean, onClick: () -> Unit) =
    SelectChip(label, selected, onClick)

/** Cosmic gradient main call to action. */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tag: String? = null,
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(CircleShape)
            .background(
                if (enabled) CosmicGradient
                else CosmicPurple.copy(alpha = 0.35f),
            )
            .then(
                if (enabled) Modifier.border(
                    1.dp,
                    CosmicGlow.copy(alpha = 0.65f),
                    CircleShape,
                ) else Modifier,
            )
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .then(if (tag != null) Modifier.testTag(tag) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text,
            style = FcType.Button,
            color = if (enabled) Color.White else Color.White.copy(alpha = 0.4f),
        )
    }
}

/** A cosmic text button in a dialog. */
@Composable
fun DialogButton(
    text: String,
    onClick: () -> Unit,
    filled: Boolean,
    destructive: Boolean = false,
    enabled: Boolean = true,
) {
    Box(
        Modifier
            .height(40.dp)
            .widthIn(min = 80.dp)
            .clip(CircleShape)
            .then(
                if (filled) {
                    Modifier.background(
                        if (destructive) Fc.Negative
                        else CosmicGradient,
                    )
                } else {
                    Modifier
                        .background(CosmicSurface)
                        .border(1.dp, CosmicBorder, CircleShape)
                },
            )
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text,
            style = FcType.Button,
            color = Color.White.copy(alpha = if (enabled) 1f else 0.4f),
        )
    }
}

/** A cosmic dialog with a title, content and Cancel / OK buttons. */
@Composable
fun FcDialog(
    title: String,
    confirm: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismiss: String = "Cancel",
    destructive: Boolean = false,
    confirmEnabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            Modifier
                .padding(horizontal = 32.dp)
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(CosmicPanelGradient)
                .border(
                    1.dp,
                    CosmicBorder,
                    RoundedCornerShape(28.dp),
                )
                .padding(24.dp),
        ) {
            Text(
                title,
                style = FcType.DialogTitle,
                color = CosmicGlow,
            )

            Spacer(Modifier.height(16.dp))

            content()

            Spacer(Modifier.height(24.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
            ) {
                DialogButton(dismiss, onDismiss, filled = false)

                DialogButton(
                    confirm,
                    onConfirm,
                    filled = true,
                    destructive = destructive,
                    enabled = confirmEnabled,
                )
            }
        }
    }
}

/** A cosmic preset tile for filters, effects and transitions. */
@Composable
fun PresetTile(
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
    tag: String? = null,
    picture: @Composable BoxScope.() -> Unit,
) {
    Column(
        Modifier
            .width(84.dp)
            .clickable(role = Role.RadioButton, onClick = onClick)
            .then(if (tag != null) Modifier.testTag(tag) else Modifier)
            .semantics { this.selected = selected },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier
                .size(84.dp)
                .then(
                    if (selected) {
                        Modifier.border(
                            2.dp,
                            CosmicGlow,
                            RoundedCornerShape(16.dp),
                        )
                    } else {
                        Modifier.border(
                            1.dp,
                            CosmicBorder,
                            RoundedCornerShape(16.dp),
                        )
                    },
                )
                .padding(4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFF28204A),
                            Color(0xFF11182F),
                        ),
                    ),
                ),
            contentAlignment = Alignment.Center,
            content = picture,
        )

        Spacer(Modifier.height(4.dp))

        Text(
            name,
            style = FcType.TileLabel,
            color = if (selected) CosmicGlow else Fc.Title.copy(alpha = 0.75f),
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.requiredWidth(96.dp),
        )
    }
}

/** A cosmic labelled slider row. */
@Composable
fun SliderRow(
    label: String,
    value: String,
    content: @Composable () -> Unit,
) {
    Column(Modifier.fillMaxWidth().padding(horizontal = FcSize.PanelPadding)) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                label,
                style = FcType.Label,
                color = Fc.Secondary,
                modifier = Modifier.weight(1f),
            )

            Text(
                value,
                style = FcType.Value,
                color = CosmicGlow,
            )
        }

        Spacer(Modifier.height(5.dp))

        content()
    }
}
