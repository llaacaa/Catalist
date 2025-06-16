package com.example.catalist.data.local

import com.example.catalist.data.model.CatListResponseItemDto
import com.example.catalist.data.model.WeightDto
import java.util.UUID


fun CatListResponseItemDto.toEntity(): CatInfoEntity {
    return CatInfoEntity(
        id = id ?: UUID.randomUUID().toString(),
        adaptability = adaptability,
        affection_level = affection_level,
        alt_names = alt_names,
        bidability = bidability,
        cat_friendly = cat_friendly,
        cfa_url = cfa_url,
        child_friendly = child_friendly,
        country_code = country_code,
        country_codes = country_codes,
        description = description,
        dog_friendly = dog_friendly,
        energy_level = energy_level,
        experimental = experimental,
        grooming = grooming,
        hairless = hairless,
        health_issues = health_issues,
        hypoallergenic = hypoallergenic,
        indoor = indoor,
        intelligence = intelligence,
        lap = lap,
        life_span = life_span,
        name = name,
        natural = natural,
        origin = origin,
        rare = rare,
        reference_image_id = reference_image_id,
        rex = rex,
        shedding_level = shedding_level,
        short_legs = short_legs,
        social_needs = social_needs,
        stranger_friendly = stranger_friendly,
        suppressed_tail = suppressed_tail,
        temperament = temperament,
        vcahospitals_url = vcahospitals_url,
        vetstreet_url = vetstreet_url,
        vocalisation = vocalisation,
        weightImperial = weight?.imperial,
        weightMetric = weight?.metric,
        wikipedia_url = wikipedia_url
    )
}

fun CatInfoEntity.toDto(): CatListResponseItemDto {
    return CatListResponseItemDto(
        id = id,
        adaptability = adaptability,
        affection_level = affection_level,
        alt_names = alt_names,
        bidability = bidability,
        cat_friendly = cat_friendly,
        cfa_url = cfa_url,
        child_friendly = child_friendly,
        country_code = country_code,
        country_codes = country_codes,
        description = description,
        dog_friendly = dog_friendly,
        energy_level = energy_level,
        experimental = experimental,
        grooming = grooming,
        hairless = hairless,
        health_issues = health_issues,
        hypoallergenic = hypoallergenic,
        indoor = indoor,
        intelligence = intelligence,
        lap = lap,
        life_span = life_span,
        name = name,
        natural = natural,
        origin = origin,
        rare = rare,
        reference_image_id = reference_image_id,
        rex = rex,
        shedding_level = shedding_level,
        short_legs = short_legs,
        social_needs = social_needs,
        stranger_friendly = stranger_friendly,
        suppressed_tail = suppressed_tail,
        temperament = temperament,
        vcahospitals_url = vcahospitals_url,
        vetstreet_url = vetstreet_url,
        vocalisation = vocalisation,
        weight =  WeightDto(
            imperial = weightImperial,
            metric = weightMetric
        ),
        wikipedia_url = wikipedia_url
    )
}