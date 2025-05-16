<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Notifications\Notifiable;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class Juego extends Model
{
    use HasFactory;

    protected $table = 'juegos';
    protected $primaryKey = 'IDJuego';


    protected $fillable = [
        'IDJuego',
        'NombreJuego',
        'Genero',
        'Descripcion'
    ];

    public function usuario(): BelongsToMany
    {
        return $this->belongsToMany(Usuario::class, 'juegousuario', 'IDJuego', 'IDUsuario')
            ->withPivot('Estadisticas', 'Preferencias', 'NivelElo');
    }

}
