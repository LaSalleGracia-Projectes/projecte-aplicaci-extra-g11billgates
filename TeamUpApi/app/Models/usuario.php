<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Notifications\Notifiable;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;
use Laravel\Sanctum\HasApiTokens;
use Illuminate\Foundation\Auth\User as Authenticatable;

class Usuario extends Model
{
    use HasFactory;
    use HasApiTokens;

    protected $fillable = [
        'IDUsuario',
        'Nombre',
        'Correo',
        'Contraseña',
        'FotoPerfil',
        'Edad',
        'Region'
    ];
    protected $hidden = [
        'Contraseña'
    ];
    //funcion para encriptar contraseña
    protected function cast(): array
    {
        return [
            'Contraseña' => 'hashed'
        ];
    }
    //relacion a MatchUsers 1:n
    public function matchUsers()
    {
        return $this->hasMany(MatchUsers::class);
    }
    //relacion a juego n:m
    public function juego(): BelongsToMany
    {
        return $this->belongsToMany(Juego::class)->withPivot('Estadisticas', 'Preferencias', 'NivelElo');
    }
    // relacion a mensaje 1:n
    public function mensaje()
    {
        return $this->hasMany(Mensaje::class);
    }
    //relacion a actividad 1:n
    public function actividad()
    {
        return $this->hasMany(Actividad::class);
    }
}
