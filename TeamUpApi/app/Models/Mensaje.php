<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Notifications\Notifiable;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class Mensaje extends Model
{
    use HasFactory;

    protected $primaryKey = 'IDMensaje';
    public $incrementing = true; // o false si no es auto-incremental
    public $timestamps = false; // si no usas created_at y updated_at

    protected $fillable = [
        'IDMensaje',
        'IDChat',
        'IDUsuario',
        'Tipo',
        'FechaEnvio',
        'Texto',
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
