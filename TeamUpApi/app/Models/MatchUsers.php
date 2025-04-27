<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Notifications\Notifiable;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class MatchUsers extends Model
{
    use HasFactory;
    protected $table = 'matchusers';

    protected $fillable = [
        'IDMatch',
        'IDUsuario1',
        'IDUsuario2',
        'FechaCreacion'
    ];
    //relacion al usuario 1 1:n
    public function usuario1()
    {
        return $this->belongsTo(Usuario::class, 'IDUsuario1', 'IDUsuario');
    }
    //relacion al usuario 2 1:n
    public function usuario2()
    {
        return $this->belongsTo(Usuario::class, 'IDUsuario2', 'IDUsuario');
    }
    //relacion a chat 1:1
    public function chat()
    {
        return $this->hasOne(Chat::class);
    }
}
