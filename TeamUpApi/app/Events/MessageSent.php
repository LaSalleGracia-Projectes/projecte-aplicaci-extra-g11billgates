<?php

namespace App\Events;

use App\Models\Mensaje; // 👈 Asegúrate de importar tu modelo
use Illuminate\Broadcasting\Channel;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class MessageSent implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;

    public $message;

    /**
     * Create a new event instance.
     */
    public function __construct(Mensaje $message)
    {
        $this->message = $message;
    }

    /**
     * Get the channels the event should broadcast on.
     */
    public function broadcastOn()
    {
        return new Channel('chat');
    }

    public function broadcastAs()
    {
        return 'message.sent';
    }

    /**
     * Define the data to broadcast.
     */
    public function broadcastWith()
    {
        return [
            'IDMensaje' => $this->message->IDMensaje,
            'IDChat' => $this->message->IDChat,
            'IDUsuario' => $this->message->IDUsuario,
            'Tipo' => $this->message->Tipo,
            'FechaEnvio' => $this->message->FechaEnvio,
        ];
    }
}
