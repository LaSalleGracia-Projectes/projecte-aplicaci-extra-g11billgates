<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Notifications\Notifiable;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class MatchUsers extends Model
{
    use HasFactory;

    protected $primaryKey = 'IDMatch';

    public $timestamps = false;

    protected $fillable = [
        'IDMatch',
        'IDUsuario1',
        'IDUsuario2',
        'FechaCreacion'
    ];
    public function usuario1()
    {
        return $this->belongsTo(Usuario::class, 'IDUsuario1', 'id');
    }

    public function usuario2()
    {
        return $this->belongsTo(Usuario::class, 'IDUsuario2', 'id');
    }
    //relacion a chat 1:1
    public function chat()
    {
        return $this->hasOne(Chat::class);
    }
}
