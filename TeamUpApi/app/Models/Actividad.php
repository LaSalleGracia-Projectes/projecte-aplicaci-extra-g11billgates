<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Notifications\Notifiable;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class Actividad extends Model
{
    use HasFactory;

    protected $fillable = [
        'IDActividad',
        'IDUsuario',
        'TipoActividad',
        'FechaRegistro'
    ];
    public function usuario()
    {
        return $this->belongsTo(Usuario::class, 'IDUsuario', 'IDUsuario');
    }
}
