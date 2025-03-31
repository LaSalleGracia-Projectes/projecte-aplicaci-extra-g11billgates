<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Notifications\Notifiable;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class Juego extends Model
{
    use HasFactory;

    protected $fillable = [
        'IDJuego',
        'NombreJuego',
        'Genero',
        'Descripcion'
    ];

    //relcion a usuario n:m tabla pivote juegousuario
    public function usuario(): BelongsToMany
    {
        return $this->belongsToMany(Juego::class)->withPivot('Estadisticas', 'Preferencias', 'NivelElo');
    }
}
