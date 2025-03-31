<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Notifications\Notifiable;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class Mensaje extends Model
{
    use HasFactory;

    protected $fillable = [
        'IDMensaje',
        'IDChat',
        'IDUsuario',
        'Tipo',
        'FechaEnvio'
    ];

    //relacion a chat 1:n
    public function chat()
    {
        return $this->belongsTo(Chat::class, 'IDChat', 'IDChat');
    }
    //relacion a usuario 1:n
    public function usuario()
    {
        return $this->belongsTo(Usuario::class, 'IDUsuario', 'IDUsuario');
    }
}
