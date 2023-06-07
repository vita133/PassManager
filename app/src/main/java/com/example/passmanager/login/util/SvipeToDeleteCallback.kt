package com.example.passmanager.login.util
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.passmanager.R

class SwipeToDeleteCallback(private val adapter: PasswordAdapter) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlags = ItemTouchHelper.START
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val backgroundColor = ContextCompat.getColor(itemView.context, R.color.red)
        val background = ColorDrawable(backgroundColor)
        val backgroundLeft: Int
        val backgroundRight: Int
        if (dX > 0) {
            backgroundLeft = itemView.left
            backgroundRight = itemView.left + dX.toInt()
        } else {
            backgroundLeft = itemView.right + dX.toInt()
            backgroundRight = itemView.right
        }

        background.setBounds(backgroundLeft, itemView.top, backgroundRight, itemView.bottom)
        background.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.showDeleteConfirmationDialog(viewHolder.itemView.context, position)
    }
}
